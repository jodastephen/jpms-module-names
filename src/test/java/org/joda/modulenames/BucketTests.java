package org.joda.modulenames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class BucketTests {

  @Test
  void createBucketWithWrongNameFails() {
    var name = "bucket-name-that-does-not-exist";
    var e = assertThrows(IllegalArgumentException.class, () -> new Bucket(name, "us-east-1"));
    assertEquals("Bucket " + name + " not found in us-east-1", e.getMessage());
  }

  @Test
  void getKeys1To10_inRoot() {
    getKeys1To10("");
  }

  private void getKeys1To10(String prefix) {
    try (var bucket = bucket()) {
      var actualKeys = bucket.getKeys(prefix, 10, "");
      assertLinesMatch(
          List.of(
              prefix + "modulescanner-report-2018_08_18_00_58_06.csv",
              prefix + "modulescanner-report-2018_08_20_19_49_05.csv",
              prefix + "modulescanner-report-2018_08_20_20_23_29.csv",
              prefix + "modulescanner-report-2018_08_20_20_24_45.csv",
              prefix + "modulescanner-report-2018_08_20_20_33_12.csv",
              prefix + "modulescanner-report-2018_08_20_20_35_15.csv",
              prefix + "modulescanner-report-2018_08_20_21_08_53.csv",
              prefix + "modulescanner-report-2018_08_20_21_23_19.csv",
              prefix + "modulescanner-report-2018_08_20_21_35_31.csv",
              prefix + "modulescanner-report-2018_08_20_21_41_55.csv"),
          actualKeys);
    }
    var logger = System.getLogger(Bucket.class.getName());
    if (logger instanceof CollectingLoggerFinder.Logger) {
      var logs = ((CollectingLoggerFinder.Logger) logger).getLogs();
      logs.stream().map(CollectingLoggerFinder.Log::getMessage).forEach(System.out::println);
      logs.clear();
    }
  }

  private static Bucket bucket() {
    try {
      return new Bucket("java9plusadoption", "us-east-1");
    } catch (IOException e) {
      fail("Should not happen!", e);
      throw new AssertionError("Should also not happen!");
    }
  }
}
