package org.joda.modulenames;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Bucket implements AutoCloseable {

  private static final System.Logger LOG = System.getLogger(Bucket.class.getName());
  private static final int MAX_KEYS_PER_PAGE = 1000;

  private final String bucketName;
  private final AmazonS3 s3;

  Bucket(String bucketName, String bucketRegion) {
    this.bucketName = bucketName;
    this.s3 = AmazonS3ClientBuilder.standard().withRegion(bucketRegion).build();
  }

  @Override
  public void close() {
    s3.shutdown();
  }

  List<String> getKeys(int limit, String after) {
    LOG.log(INFO, "Get keys from {0} bucket (limit={1}, after={2})...", bucketName, limit, after);
    var keys = new ArrayList<String>();
    var bytes = 0L;
    var request = new ListObjectsV2Request().withBucketName(bucketName);
    if (!after.isBlank()) {
      LOG.log(INFO, "Set start after to: {0}", after);
      request.setStartAfter(after);
    }
    while (true) {
      var pendingKeys = limit - keys.size();
      if (pendingKeys <= 0) {
        LOG.log(DEBUG, "No more keys are pending: done.");
        break;
      }
      request.setMaxKeys(Math.min(pendingKeys, MAX_KEYS_PER_PAGE));
      LOG.log(INFO, "Get objects list... (max={0}, token={1})", request.getMaxKeys(), request.getContinuationToken());
      var objects = s3.listObjectsV2(request);
      var summaries = objects.getObjectSummaries();
      for (var summary : summaries) {
        LOG.log(DEBUG, "   o {0} (bytes: {1})", summary.getKey(), summary.getSize());
        keys.add(summary.getKey());
        bytes += summary.getSize();
      }
      LOG.log(INFO, " - {0} objects retrieved", keys.size());
      if (!objects.isTruncated()) {
        LOG.log(DEBUG, "Objects result is not truncated: done.");
        break;
      }
      request.setContinuationToken(objects.getNextContinuationToken());
    }
    LOG.log(INFO, "Got {0} keys (bytes: {1})", keys.size(), bytes);
    return keys;
  }

  void processObject(String key, Consumer<String> consumeLine) {
    var home = Path.of(System.getProperty("user.home"));
    var cache = home.resolve(".jpms-module-names").resolve(bucketName);
    var csv = cache.resolve(key);
    try {
      if (Files.notExists(csv)) {
        LOG.log(INFO, "Downloading {0}...", key);
        Files.createDirectories(cache);
        try (var object = s3.getObject(new GetObjectRequest(bucketName, key))) {
          LOG.log(DEBUG, "Content-Type: {0}", object.getObjectMetadata().getContentType());
          Files.copy(object.getObjectContent().getDelegateStream(), csv);
        }
        LOG.log(INFO, "Loaded {0} bytes to {1}", Files.size(csv), csv);
      }
      Files.readAllLines(csv).forEach(consumeLine);
    } catch (IOException e) {
      throw new UncheckedIOException("Processing object failed: " + key, e);
    }
  }
}
