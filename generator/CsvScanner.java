import static java.util.stream.Collectors.toList;

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/** Scan a CSV file and update generated property files. */
public class CsvScanner {

  public static void main(String... args) throws Exception {
    var generator = new MapGenerator();
    generator.build(Path.of("out", "modulescanner-output.csv"));
    System.out.println();
    System.out.println(generator.uniques.size() + " unique modules found");
    System.out.println(generator.syntax.size() + " module names are syntactically invalid");
    System.out.println(generator.javas.size() + " module names start with 'java[x].'");
    System.out.println(generator.simple.size() + " module names don't contain a single dot ('.')");
    System.out.println(generator.duplicates.size() + " module names are not unique");
    System.out.println(generator.impostors.size() + " modules are impostors!");

    var mavens = new PropertiesWriter(Path.of("generated", "module-maven.properties"));
    var versions = new PropertiesWriter(Path.of("generated", "module-version.properties"));

    generator
        .uniques
        .values()
        .forEach(
            item -> {
              mavens.put(item, e -> e.mavenGroupColonArtifact, false);
              versions.put(item, e -> e.mavenVersion, true);
            });
    mavens.write();
    versions.write();
    System.out.println();
    System.out.printf("Wrote %d lines to %s %n", mavens.map.size(), mavens.path);
    System.out.printf("Wrote %d lines to %s %n", versions.map.size(), versions.path);

    write(Path.of("error", "error-syntax.txt"), generator.syntax);
    write(Path.of("error", "error-simple.txt"), generator.simple);
    write(Path.of("error", "error-javas.txt"), generator.javas);
    write(Path.of("error", "error-impostors.txt"), generator.impostors.values());
    write(Path.of("error", "error-duplicates.txt"), generator.duplicates.values());
  }

  private static void write(Path path, Collection<?> collection) throws Exception {
    Files.createDirectories(path.getParent());
    Files.write(path, collection.stream().map(Object::toString).sorted().collect(toList()));
  }

  private static class PropertiesWriter {
    private final Path path;
    private final Map<String, String> map;

    PropertiesWriter(Path path) {
      this.path = path;
      this.map = loadFromProperties(path);
    }

    void put(Item item, Function<Item, String> function, boolean allowUpdates) {
      var key = item.moduleName;
      var value = function.apply(item);
      var old = map.put(key, value);
      if (old != null) {
        if (allowUpdates || old.equals(value)) {
          return;
        }
        throw new IllegalStateException("Updates are not allowed for: " + key);
      }
    }

    void write() throws Exception {
      var lines =
          map.entrySet()
              .stream()
              .sorted(Comparator.comparing(Map.Entry::getKey))
              .map(e -> e.getKey() + '=' + e.getValue())
              .collect(toList());
      Files.write(path, lines);
    }
  }

  private static class MapGenerator {

    final Map<String, List<Item>> duplicates = new TreeMap<>();
    final Map<String, List<Item>> impostors = new TreeMap<>();
    final Map<String, Item> uniques = new TreeMap<>();
    final List<Item> syntax = new ArrayList<>();
    final List<Item> javas = new ArrayList<>();
    final List<Item> simple = new ArrayList<>();

    private final Map<String, String> startOfModuleNamesToMavenGroupMap =
        loadFromProperties(Path.of("etc", "module-name-to-maven-group.properties"));

    private void build(Path path) throws Exception {
      try (var lines = Files.lines(path)) {
        lines.forEach(this::handle);
      }
      duplicates.keySet().forEach(uniques::remove);
    }

    private void handle(String line) {
      if (line.startsWith("groupId,artifactId,version,moduleName,moduleVersion")) {
        // Skip caption line...
        return;
      }
      var item = new Item(line);
      var moduleName = item.moduleName;
      if (moduleName.isBlank()) {
        return;
      }
      if (moduleName.indexOf('.') == -1) {
        // System.out.println("Module name doesn't contain a single dot: " + item + " // " +
        // item.moduleMode);
        simple.add(item);
        return;
      }
      if (!SourceVersion.isName(moduleName)) {
        // System.out.println("Invalid module name detected: " + item + " // " + item.moduleMode);
        syntax.add(item);
        return;
      }
      if (moduleName.startsWith("java.") || moduleName.startsWith("javax.")) {
        // System.out.println("Module names should not start with 'java[x].' " + item);
        javas.add(item);
        return;
      }
      if (isImpostor(item)) {
        // System.out.println("Impostor detected: " + item + " // " + item.moduleMode);
        impostors.computeIfAbsent(moduleName, key -> new ArrayList<>()).add(item);
        return;
      }
      var dup = duplicates.get(moduleName);
      if (dup != null) {
        dup.add(item);
        // System.out.println("Duplicate module name detected: " + item);
        return;
      }
      var old = uniques.get(moduleName);
      if (old != null) {
        if (!item.mavenGroupColonArtifact.equals(old.mavenGroupColonArtifact)) {
          var duplicateList = duplicates.computeIfAbsent(moduleName, key -> new ArrayList<>());
          duplicateList.add(old);
          duplicateList.add(item);
          return;
        }
      }
      uniques.put(moduleName, item);
    }

    private boolean isImpostor(Item item) {
      for (var entry : startOfModuleNamesToMavenGroupMap.entrySet()) {
        if (item.moduleName.startsWith(entry.getKey())) {
          return !item.mavenGroupId.equals(entry.getValue());
        }
      }
      return false;
    }
  }

  private static Map<String, String> loadFromProperties(Path path) {
    var map = new TreeMap<String, String>();
    try {
      for (var line : Files.readAllLines(path)) {
        var values = line.split("=");
        var key = values[0].trim();
        var value = values[1].trim();
        if (map.put(key, value) != null) {
          throw new IllegalStateException("Non unique key found: " + key);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Loading properties failed for: " + path, e);
    }
    return map;
  }

  private static String valueOrBlankIfDash(String value) {
    return "-".equals(value) ? "" : value;
  }

  private static class Item {

    final String mavenGroupId;
    final String mavenArtifactId;
    final String mavenVersion;
    final String mavenGroupColonArtifact;

    final String moduleName;
    final String moduleVersion;
    final String moduleMode;
    final List<String> moduleDependencies;

    final String jdepsToolError;
    final List<String> jdepsViolations;

    private Item(String line) {
      var values = line.split(",");
      //
      this.mavenGroupId = valueOrBlankIfDash(values[0]);
      this.mavenArtifactId = valueOrBlankIfDash(values[1]);
      this.mavenVersion = valueOrBlankIfDash(values[2]);
      this.mavenGroupColonArtifact = mavenGroupId + ':' + mavenArtifactId;
      //
      this.moduleName = valueOrBlankIfDash(values[3]);
      this.moduleVersion = valueOrBlankIfDash(values[4]);
      this.moduleMode = valueOrBlankIfDash(values[5]);
      this.moduleDependencies = List.of(values[6].split(" \\+ "));
      //
      this.jdepsToolError = valueOrBlankIfDash(values[7]);
      this.jdepsViolations = List.of(values[8].split(" \\+ "));
    }

    @Override
    public String toString() {
      return moduleName + '=' + mavenGroupId + ':' + mavenArtifactId + ':' + mavenVersion;
    }
  }
}
