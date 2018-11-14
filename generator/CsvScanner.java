import static java.util.stream.Collectors.toList;

import javax.lang.model.SourceVersion;
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
            entry -> {
              mavens.put(entry, e -> e.mavenGroupColonArtifact, false);
              versions.put(entry, e -> e.mavenVersion, true);
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

    PropertiesWriter(Path path) throws Exception {
      this.path = path;
      this.map = loadFromProperties(path);
    }

    void put(Entry entry, Function<Entry, String> function, boolean allowUpdates) {
      var key = entry.moduleName;
      var value = function.apply(entry);
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

    final Map<String, List<Entry>> duplicates = new TreeMap<>();
    final Map<String, List<Entry>> impostors = new TreeMap<>();
    final Map<String, Entry> uniques = new TreeMap<>();
    final List<Entry> syntax = new ArrayList<>();
    final List<Entry> javas = new ArrayList<>();
    final List<Entry> simple = new ArrayList<>();

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
      var entry = new Entry(line);
      var moduleName = entry.moduleName;
      if (moduleName.isBlank()) {
        return;
      }
      if (moduleName.indexOf('.') == -1) {
        // System.out.println("Module name doesn't contain a single dot: " + entry + " // " +
        // entry.moduleMode);
        simple.add(entry);
        return;
      }
      if (!SourceVersion.isName(moduleName)) {
        // System.out.println("Invalid module name detected: " + entry + " // " + entry.moduleMode);
        syntax.add(entry);
        return;
      }
      if (moduleName.startsWith("java.") || moduleName.startsWith("javax.")) {
        // System.out.println("Module names should not start with 'java[x].' " + entry);
        javas.add(entry);
        return;
      }
      if (!isExpectedModuleName(entry)) {
        // System.out.println("Impostor detected: " + entry + " // " + entry.moduleMode);
        impostors.computeIfAbsent(moduleName, key -> new ArrayList<>()).add(entry);
        return;
      }
      var dup = duplicates.get(moduleName);
      if (dup != null) {
        dup.add(entry);
        // System.out.println("Duplicate module name detected: " + entry);
        return;
      }
      var old = uniques.get(moduleName);
      if (old != null) {
        if (!entry.mavenGroupColonArtifact.equals(old.mavenGroupColonArtifact)) {
          var duplicateList = duplicates.computeIfAbsent(moduleName, key -> new ArrayList<>());
          duplicateList.add(old);
          duplicateList.add(entry);
          return;
        }
      }
      uniques.put(moduleName, entry);
    }
  }

  private static Map<String, String> loadFromProperties(Path path) throws Exception {
    var map = new TreeMap<String, String>();
    for (var line : Files.readAllLines(path)) {
      var values = line.split("=");
      var key = values[0].trim();
      var value = values[1].trim();
      if (map.put(key, value) != null) {
        throw new IllegalStateException("Non unique key found: " + key);
      }
    }
    return map;
  }

  private static boolean isExpectedModuleName(Entry e) {
    // ASM
    if (e.moduleName.startsWith("org.objectweb.asm")) {
      return "org.ow2.asm".equals(e.mavenGroupId);
    }
    // Apache Commons
    if (e.moduleName.startsWith("org.apache.commons")) {
      return "org.apache.commons".equals(e.mavenGroupId);
    }
    // Apache Log4J
    if (e.moduleName.startsWith("org.apache.logging.log4j")) {
      return "org.apache.logging.log4j".equals(e.mavenGroupId);
    }
    // ByteBuddy
    if (e.moduleName.startsWith("net.bytebuddy")) {
      return "net.bytebuddy".equals(e.mavenGroupId);
    }
    // SLF4J
    if (e.moduleName.startsWith("org.slf4j")) {
      return "org.slf4j".equals(e.mavenGroupId);
    }
    return true;
  }

  private static String valueOrBlankIfDash(String value) {
    return "-".equals(value) ? "" : value;
  }

  private static class Entry {

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

    private Entry(String line) {
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
