package org.joda.modulenames;

import static java.lang.System.Logger.Level.DEBUG;
import static java.util.function.Predicate.not;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

class Utility {

  private static final System.Logger LOG = System.getLogger(Utility.class.getName());

  static Map<String, String> loadFromProperties(Path path) {
    return loadFromProperties(path, Objects::toString);
  }

  static <V> Map<String, V> loadFromProperties(Path path, Function<String, V> valueFactory) {
    LOG.log(DEBUG, "Loading properties from `%s`...", path);
    var map = new TreeMap<String, V>();
    if (Files.notExists(path)) {
      return map;
    }
    try (var lines = Files.lines(path)) {
      lines
          .map(String::trim)
          .filter(not(String::isEmpty))
          .filter(line -> line.charAt(0) != '#')
          .forEach(
              line -> {
                var values = line.split("=");
                var key = values[0].trim();
                var value = valueFactory.apply(values[1].trim());
                if (map.put(key, value) != null) {
                  throw new IllegalStateException("Duplicate key found: " + key);
                }
              });
    } catch (IOException e) {
      throw new UncheckedIOException("Loading properties failed for: " + path, e);
    }
    LOG.log(DEBUG, "Loaded {0} properties from: {1}", map.size(), path);
    return map;
  }

  static String loadString(Path path) {
    LOG.log(DEBUG, "Loading string from `%s`...", path);
    if (Files.notExists(path)) {
      return "";
    }
    try {
      return Files.readString(path);
    } catch (IOException e) {
      throw new UncheckedIOException("Loading string failed for: " + path, e);
    }
  }

  private Utility() {
    throw new Error();
  }
}
