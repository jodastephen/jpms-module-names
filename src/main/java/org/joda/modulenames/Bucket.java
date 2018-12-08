package org.joda.modulenames;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
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

    try {
      if (!s3.doesBucketExistV2(bucketName)) {
        var message = String.format("Bucket %s not found in %s", bucketName, bucketRegion);
        throw new IllegalArgumentException(message);
      }
    } catch (AmazonS3Exception e) {
      throw new IllegalArgumentException("Illegal bucket name or region?", e);
    }

    var loader = getClass().getClassLoader();
    this.cache = Path.of("etc", "cache", bucketName);
    var zipfile = cache.resolve(bucketName + ".zip");
    if (Files.exists(zipfile)) {
      LOG.log(INFO, "Creating cache file system from {0}...", zipfile);
      this.zipfs = FileSystems.newFileSystem(zipfile, loader);
      if (LOG.isLoggable(INFO)) {
        var count = Files.list(zipfs.getPath("/")).count();
        LOG.log(INFO, "{0} entries in {1}", count, zipfs);
      }
    } else {
      this.zipfs = null;
    }
  }

  @Override
  public void close() {
    s3.shutdown();
    if (zipfs != null) {
      try {
        zipfs.close();
      } catch (IOException e) {
        LOG.log(WARNING, "Closing file system failed!", e);
      }
    }
  }

  List<String> getKeys(String prefix, int limit, String after) {
    if (limit < 0) {
      throw new IllegalArgumentException("limit must not be negative: " + limit);
    }
    if (limit == 0) {
      return List.of();
    }
    if (after == null) {
      throw new IllegalArgumentException("after must not be null");
    }
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
      request.setDelimiter("/");
      request.setPrefix(prefix);
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
    Path path = toPath(key);
    LOG.log(INFO, "Processing {0} by reading lines from {1}...", key, path);
    Files.readAllLines(path).forEach(consumeLine);
  }

  private Path toPath(String key) throws IOException {
    if (zipfs != null) {
      var zip = zipfs.getPath(key);
      if (Files.exists(zip)) {
        LOG.log(DEBUG, "Extracting {0} from {1}...", key, zipfs);
        return zip;
      }
    }
    var csv = cache.resolve(key);
    if (Files.notExists(csv)) {
      Files.createDirectories(csv.getParent());
      LOG.log(INFO, "Downloading {0} from remote {1}...", key, bucketName);
      Files.createDirectories(cache);
      try (var object = s3.getObject(new GetObjectRequest(bucketName, key))) {
        var length = object.getObjectMetadata().getContentLength();
        LOG.log(INFO, "Loading {0} bytes to {1}...", length, csv);
        try (var stream = object.getObjectContent().getDelegateStream()) {
          Files.copy(stream, csv);
        }
      }
      LOG.log(DEBUG, "Loaded {0} bytes to {1}", Files.size(csv), csv);
    }
    return csv;
  }
}
