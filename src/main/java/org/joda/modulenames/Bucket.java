package org.joda.modulenames;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Bucket implements AutoCloseable {

  private static final System.Logger LOG = System.getLogger(Bucket.class.getName());
  private static final int MAX_KEYS_PER_PAGE = 1000;

  private final String bucketName;
  private final Path cache;
  private final AmazonS3 s3;
  private final FileSystem zipfs;

  Bucket(String bucketName, String bucketRegion) throws IOException {
    this.bucketName = bucketName;
    this.s3 = AmazonS3ClientBuilder.standard().withRegion(bucketRegion).build();

    var loader = getClass().getClassLoader();
    this.cache = Path.of("etc", "cache", bucketName);
    this.zipfs = FileSystems.newFileSystem(cache.resolve(bucketName + ".zip"), loader);

    if (LOG.isLoggable(INFO)) {
      var count = Files.list(zipfs.getPath("/")).count();
      LOG.log(INFO, "{0} entries in {1}", count, zipfs);
    }
  }

  @Override
  public void close() throws IOException {
    s3.shutdown();
    zipfs.close();
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
      LOG.log(INFO, "Get objects list... (max={0})", request.getMaxKeys());
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

  void processObject(String key, Consumer<String> consumeLine) throws IOException {
    var zip = zipfs.getPath(key);
    var csv = cache.resolve(key);
    Path path;
    if (Files.exists(zip)) {
      LOG.log(INFO, "Extracting {0} from {1}...", key, zip);
      path = zip;
    } else {
      if (Files.notExists(csv)) {
        LOG.log(INFO, "Downloading {0} from {1}...", key, bucketName);
        Files.createDirectories(cache);
        try (var object = s3.getObject(new GetObjectRequest(bucketName, key))) {
          LOG.log(DEBUG, "Content-Type: {0}", object.getObjectMetadata().getContentType());
          Files.copy(object.getObjectContent().getDelegateStream(), csv);
        }
        LOG.log(INFO, "Loaded {0} bytes to {1}", Files.size(csv), csv);
      }
      path = csv;
    }
    Files.readAllLines(path).forEach(consumeLine);
  }
}
