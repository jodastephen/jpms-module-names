import static javax.xml.xpath.XPathConstants.NODESET;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.lang.module.FindException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@SuppressWarnings("WeakerAccess")
class Generator {

  /** Single row. */
  class Row {
    String caption = ""; // org.junit.jupiter:junit-jupiter-api
    String description = ""; // Module "junit-jupiter-api" of JUnit 5.
    String homepage = ""; // https://github.com/junit-team/junit5/tree/master/junit-jupiter-api
    String module = ""; // org.junit.jupiter.api
    String group = ""; // org.junit.jupiter
    String artifact = ""; // junit-jupiter-api
    String version = ""; // 5.1.0-M1
    String uri = ""; // http://central.maven.org/maven2/.../junit-jupiter-api-5.1.0-M1.jar
    ModuleDescriptor descriptor = null;

    URI toMvnRepositoryURI() {
      var base = "https://mvnrepository.com/artifact/";
      return URI.create(base + String.join("/", group, artifact));
    }
  }

  /** Table: a list of rows. */
  class Table {
    String caption = ""; // JUnit 5
    String description = ""; // JUnit 5 is the next generation of JUnit.
    String homepage = ""; // http://junit.org/junit5/
    String group = ""; // org.junit.jupiter
    String version = ""; // 5.1.0-M1
    List<Row> rows = new ArrayList<>();

    Row add(String any) {
      // Local "/some/path/file.jar" or remote "http://host/file.jar" URI.
      if (any.endsWith(".jar")) {
        return add(URI.create(any));
      }
      // "group:artifact:version"
      var split = any.split(":");
      if (split.length == 3) {
        return add(split[0], split[1], split[2]);
      }
      // treat any as artifact and inherit group and version
      return add(group, any, version);
    }

    Row add(String artifact, String version) {
      return add(group, artifact, version);
    }

    Row add(String group, String artifact, String version) {
      var jar = findURI(group, artifact, version);
      return add(jar);
    }

    Row add(URI jar) {
      var pom = URI.create(jar.toString().replaceFirst("\\.jar$", ".pom"));
      var row = createRow(jar, pom);
      rows.add(row);
      return row;
    }

    void scan(String offset) {
      scan(offset, uri -> true);
    }

    void scan(String offset, Predicate<String> predicate) {
      scan("http://central.maven.org/maven2/", offset, predicate);
    }

    void scan(String base, String offset, Predicate<String> predicate) {
      info("Scanning {0}...", offset);
      offset = offset.replace('.', '/');
      if (!offset.endsWith("/")) {
        offset = offset + "/";
      }

      var scanner = new Scanner(predicate, this::add, URI.create(base), URI.create(offset));
      scanner.scan("");
    }

    Row raw(String caption, String module, String group, String artifact) {
      var row = new Row();
      row.caption = caption;
      row.module = module;
      row.group = group;
      row.artifact = artifact;
      rows.add(row);
      return row;
    }
  }

  /** Create rows by scanning an URI tree. */
  class Scanner {

    final Predicate<String> predicate;
    final Consumer<URI> consumer;
    final URI base;
    final URI offset;

    Scanner(Predicate<String> predicate, Consumer<URI> consumer, URI base, URI offset) {
      this.predicate = predicate;
      this.consumer = consumer;
      this.base = base;
      this.offset = offset;
    }

    void scan(String fragment) {
      var uri = base.resolve(offset).resolve(fragment);
      if (!predicate.test(uri.toString())) {
        info("predicate skips scan({0}) // {1}", fragment, uri);
        return;
      }
      info("scan({0}) // {1}", fragment, uri);
      var optionalSource = read(uri);
      if (!optionalSource.isPresent()) {
        return;
      }
      var source = optionalSource.get();
      if (source.contains("maven-metadata.xml")) {
        if (scanMetadata(uri)) {
          return;
        }
      }
      scanContents(fragment, source);
    }

    void scanContents(String fragment, String source) {
      debug("scanContents");
      final var startKey = "<a href=\"";
      for (var line : source.split("\\R")) {
        var start = line.indexOf(startKey);
        var end = line.indexOf("\" title");
        if (start < 0 || end < 0) {
          continue;
        }
        start += startKey.length();
        var name = line.substring(start, end);
        if (!name.endsWith("/")) {
          continue;
        }
        scan(fragment + name);
      }
    }

    boolean scanMetadata(URI uri) {
      var meta = read(uri.resolve("maven-metadata.xml")).orElseThrow(Error::new);
      var metaData = mapMetadata(meta);
      if (meta.contains("<versioning>")) {
        var updated = metaData.get("updated");
        if (updated.length() != 14) {
          debug("unexpected <lastUpdated> format: {0}", updated);
          return true;
        }
        if (updated.compareTo("20170101000000") < 0) {
          debug("too old: {0}", updated);
          return true;
        }
        var artifact = metaData.get("artifact");
        var version = metaData.getOrDefault("release", "");
        if (version.isEmpty()) {
          version = metaData.get("latest");
        }
        var jar = uri.resolve(version + "/" + artifact + "-" + version + ".jar");
        try {
          consumer.accept(jar);
        } catch (Exception e) {
          info("Consuming URI `{0}` failed: {1}", jar, e);
        }
        return true;
      }
      return false;
    }
  }

  public static void main(String[] args) {
    var generator = new Generator();
    generator.info("Generator -- {0}", Instant.now());

    var demo = generator.add("Demo");
    demo.description = "Demo table with fictional projects";
    demo.homepage = "https://foo.bar";
    demo.group = "foo";
    {
      var row = demo.raw("Bar", "foo.bar", "foo", "bar");
      row.uri = "http://foo.bar.jar";
      row.description = "Bar of Foo";
      row.version = "ALPHA";
    }
    demo.add("org.junit.jupiter:junit-jupiter-api:5.0.2");
    demo.add(
        "http://central.maven.org/maven2/org/slf4j/slf4j-simple/1.8.0-beta2/slf4j-simple-1.8.0-beta2.jar");
    demo.add("org.ow2.asm", "asm", "6.1-beta");

    //    var joda = generator.add("Joda");
    //    joda.scan("org/joda/");

    //    var junit = generator.add("JUnit");
    //    junit.description = "Modules published by the JUnit Team";
    //    junit.homepage = "http://junit.org";
    //    junit.scan("org/junit/");

    //    var ow2 = generator.add("OW2 Consortium");
    //    ow2.homepage = "https://www.ow2.org/";
    //    ow2.scan("org/ow2/");

    //    var asm = generator.add("ASM is an all purpose");
    //    asm.scan("org/ow2/asm", uri -> !uri.contains("-all"));

    //    var square = generator.add("Square");
    //    square.scan("com/squareup", uri -> !uri.contains("misk"));

    //    var jooq = generator.add("jOOQ");
    //    jooq.scan("org/jooq");

    if (args.length == 2) {
      var table = generator.add(args[0]);
      table.scan(args[1]);
    }

    generator
        .toPropertiesLines(row -> row.group + ':' + row.artifact + '@' + row.version)
        .forEach(System.out::println);
    generator.toValueSeparatedLines(",").forEach(System.out::println);
    generator.toValueSeparatedLines("\t").forEach(System.out::println);
    generator.toMarkdownLines().forEach(System.out::println);
  }

  final System.Logger logger = System.getLogger(getClass().getName());
  final List<Table> tables = new ArrayList<>();

  Table add(String caption) {
    var table = createTable(caption);
    tables.add(table);
    return table;
  }

  Table createTable(String caption) {
    var table = new Table();
    table.caption = caption;
    return table;
  }

  Row createRow(URI jar, URI pom) {
    var descriptor = describeModule(jar);
    if (!descriptor.isPresent()) {
      throw new IllegalArgumentException("createRow failed for: " + jar);
    }
    var mvn = mapPom(pom);
    var row = new Row();
    row.uri = jar.toString();
    row.descriptor = descriptor.get();
    row.caption = mvn.getOrDefault("name", row.descriptor.toString());
    row.description = mvn.getOrDefault("description", "");
    row.homepage = mvn.getOrDefault("url", "");
    row.module = row.descriptor.name();
    row.group = mvn.get("group");
    row.artifact = mvn.get("artifact");
    row.version = mvn.getOrDefault("version", "");
    return row;
  }

  int dump(String directory) {
    var size = -1;
    var target = Paths.get(directory);
    try {
      target = Files.createDirectories(target);
      Files.write(target.resolve("modules.md"), toMarkdownLines());
      Files.write(target.resolve("modules.csv"), toValueSeparatedLines(","));
      Files.write(target.resolve("modules.tsv"), toValueSeparatedLines("\t"));
      var maven = toPropertiesLines(it -> it.group + ":" + it.artifact);
      Files.write(target.resolve("module-maven.properties"), maven);
      Files.write(target.resolve("module-version.properties"), toPropertiesLines(it -> it.version));
      size = maven.size();
    } catch (Exception e) {
      info("dump({0}) failed: {1}", target, e);
    }
    return size;
  }

  List<String> toMarkdownLines() {
    return toMarkdownLines(tables);
  }

  List<String> toMarkdownLines(List<Table> tables) {
    List<String> list = new ArrayList<>();
    // TODO add legend
    for (var table : tables) {
      list.add("");
      list.add("## " + toMarkdownLink(table.caption, URI.create(table.homepage)));
      if (!table.description.isEmpty()) {
        list.add(table.description);
      }
      list.add("");
      list.add("| Project | JPMS Module Name | Maven Group, Artifact and Version |");
      list.add("|---------|------------------|-----------------------------------|");
      String project;
      String icon;
      String module;
      String group;
      String artifact;
      String version;
      for (var row : table.rows) {
        var resolved = row.descriptor != null;
        // project
        project = row.caption;
        if (!row.homepage.isEmpty()) {
          try {
            project = toMarkdownLink(project, URI.create(row.homepage));
          } catch (IllegalArgumentException e) {
            System.err.println(project + ": not a valid homepage URI? " + row.homepage);
          }
        }
        // icon + module name
        icon = "";
        if (resolved) {
          icon = row.descriptor.isAutomatic() ? ":cd:" : ":dvd:";
        }
        module = icon + "`" + row.module + "`";
        module = resolved ? "**" + module + "**" : "*" + module + "*";
        // maven coordinates
        group = row.group;
        artifact = row.artifact;
        version = row.version;
        List<String> coordinates = new ArrayList<>();
        coordinates.add(group);
        coordinates.add(toMarkdownLink(artifact, row.toMvnRepositoryURI()));
        if (!version.isEmpty()) {
          coordinates.add(version);
        }
        var maven = String.join("<br>", coordinates);
        // append string
        list.add(String.format("| %s | %s | %s |", project, module, maven));
      }
    }
    return list;
  }

  String toMarkdownLink(String text, URI uri) {
    return String.format("[%s](%s)", text, uri);
  }

  List<String> toPropertiesLines(Function<Row, String> renderer) {
    return toPropertiesLines(tables, renderer);
  }

  List<String> toPropertiesLines(List<Table> tables, Function<Row, String> renderer) {
    var map = new TreeMap<>();
    for (var table : tables) {
      for (var row : table.rows) {
        var key = row.module;
        var value = renderer.apply(row);
        if (key.isEmpty() || value.isEmpty()) {
          continue;
        }
        if (map.containsKey(key)) {
          info("Module name {0} already taken by {1}", key, map.get(key));
        }
        map.put(key, value);
      }
    }
    return map.entrySet()
        .stream()
        .map(e -> e.getKey() + "=" + e.getValue())
        .collect(Collectors.toList());
  }

  List<String> toValueSeparatedLines(String delimiter) {
    return toValueSeparatedLines(tables, delimiter);
  }

  List<String> toValueSeparatedLines(List<Table> tables, String delimiter) {
    var list = new ArrayList<String>();
    list.add(String.join(delimiter, "Module", "Project", "Group ID", "Artifact ID", "Version"));
    var line = new ArrayList<String>();
    for (var table : tables) {
      for (var row : table.rows) {
        line.clear();
        line.add(row.module);
        line.add(row.caption.replace(delimiter, " "));
        line.add(row.group);
        line.add(row.artifact);
        line.add(row.version);
        list.add(String.join(delimiter, line));
      }
    }
    Collections.sort(list);
    return list;
  }

  void debug(String format, Object... args) {
    logger.log(System.Logger.Level.DEBUG, format, args);
  }

  void info(String format, Object... args) {
    logger.log(System.Logger.Level.INFO, format, args);
  }

  Optional<ModuleDescriptor> describeModule(URI uri) {
    var path = load(uri);
    if (!path.isPresent()) {
      return Optional.empty();
    }
    try {
      debug("describeModule({0} -> {1})", uri, path);
      return describeModule(path.get(), true);
    } finally {
      try {
        Files.delete(path.get());
      } catch (Exception e) {
        debug("deleting temp file failed: {0}", e);
      }
    }
  }

  Optional<ModuleDescriptor> describeModule(Path path, boolean reportFileNameBasedModuleAsEmpty) {
    try {
      var allModules = ModuleFinder.of(path).findAll();
      if (allModules.size() != 1) {
        throw new IllegalStateException("expected to find single module, but got: " + allModules);
      }
      var reference = allModules.iterator().next();
      var descriptor = reference.descriptor();
      info("describeModule({0} -> {1})", path, descriptor);
      if (reportFileNameBasedModuleAsEmpty) {
        if (descriptor.isAutomatic()) {
          if (!isAutomaticModuleNameAttributeAvailable(reference)) {
            return Optional.empty();
          }
        }
      }
      return Optional.of(descriptor);
    } catch (FindException e) {
      debug("finding module(s) failed: {0}", e);
      return Optional.empty();
    }
  }

  boolean isAutomaticModuleNameAttributeAvailable(ModuleReference moduleReference) {
    try (var moduleReader = moduleReference.open()) {
      var manifestString =
          moduleReader
              .read("META-INF/MANIFEST.MF")
              .map(StandardCharsets.UTF_8::decode)
              .map(Object::toString)
              .orElse("");
      if (manifestString.contains("Automatic-Module-Name")) {
        return true;
      }
    } catch (Exception e) {
      debug("reading manifest failed: {0}", e);
    }
    return false;
  }

  /** Extract specific POM-related values from the URI into a map. */
  Map<String, String> mapPom(URI pom) {
    return mapPom(read(pom).orElseThrow(() -> new Error("mapPom failed: " + pom)));
  }

  /** Extract specific POM-related values from a XML-String into a map. */
  Map<String, String> mapPom(String pom) {
    try {
      var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      var document = builder.parse(new InputSource(new StringReader(pom)));
      var xpath = XPathFactory.newInstance().newXPath();
      var name = xpath.evaluate("/project/name", document);
      var url = xpath.evaluate("/project/url", document);
      var group = xpath.evaluate("/project/groupId", document);
      var artifact = xpath.evaluate("/project/artifactId", document);
      var version = xpath.evaluate("/project/version", document);
      if (group.isEmpty()) {
        group = xpath.evaluate("/project/parent/groupId", document);
      }
      if (version.isEmpty()) {
        version = xpath.evaluate("/project/parent/version", document);
      }
      // expand `${...}` properties...
      var nodes = (NodeList) xpath.evaluate("/project/properties/*", document, NODESET);
      Map<String, String> properties = new HashMap<>();
      for (var i = 0; i < nodes.getLength(); i++) {
        var node = nodes.item(i);
        properties.put("${" + node.getNodeName() + "}", node.getTextContent().trim());
      }
      // manually add common "project.xxx" values
      properties.put("${project.groupId}", group);
      properties.put("${project.artifactId}", artifact);
      properties.put("${project.version}", version);
      // now expand
      name = mapPomExpandProperties(name, properties);
      url = mapPomExpandProperties(url, properties);
      group = mapPomExpandProperties(group, properties);
      artifact = mapPomExpandProperties(artifact, properties);
      version = mapPomExpandProperties(version, properties);
      // create final map
      return Map.of(
          "name", name, "url", url, "group", group, "artifact", artifact, "version", version);
    } catch (Exception e) {
      debug("scan({0}) failed: {0}", pom, e);
    }
    return Map.of();
  }

  /** Extract specific metadata values from a XML-String into a map. */
  Map<String, String> mapMetadata(String pom) {
    try {
      var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      var document = builder.parse(new InputSource(new StringReader(pom)));
      var xpath = XPathFactory.newInstance().newXPath();
      // TODO expand `${...}` properties in metadata?
      return Map.of(
          "group",
          xpath.evaluate("/metadata/groupId", document),
          "artifact",
          xpath.evaluate("/metadata/artifactId", document),
          "latest",
          xpath.evaluate("/metadata/versioning/latest", document),
          "release",
          xpath.evaluate("/metadata/versioning/release", document),
          "updated",
          xpath.evaluate("/metadata/versioning/lastUpdated", document));
    } catch (Exception e) {
      debug("scan({0}) failed: {0}", pom, e);
    }
    return Map.of();
  }

  String mapPomExpandProperties(String text, Map<String, String> properties) {
    var result = text;
    if (result.contains("${")) {
      for (var property : properties.entrySet()) {
        result = result.replace(property.getKey(), property.getValue());
      }
    }
    return result;
  }

  /** Read content from specified uri as a string. */
  Optional<String> read(URI uri) {
    try (var input = uri.toURL().openStream();
        var output = new ByteArrayOutputStream()) {
      input.transferTo(output);
      return Optional.of(output.toString("UTF-8"));
    } catch (Exception e) {
      debug("read({0}) failed: {1}", uri, e);
      return Optional.empty();
    }
  }

  /** Load content from specified uri into temporary file. */
  Optional<Path> load(URI uri) {
    try {
      var url = uri.toURL();
      var parts = url.getFile().split("/");
      var temp = Files.createTempFile("", "-" + parts[parts.length - 1]);
      try (var sourceStream = url.openStream();
          var targetStream = Files.newOutputStream(temp)) {
        sourceStream.transferTo(targetStream);
      } catch (Exception e) {
        debug("load/transfer({0}) failed: {1}", uri, e);
        Files.deleteIfExists(temp);
        return Optional.empty();
      }
      return Optional.of(temp);
    } catch (Exception e) {
      debug("load({0}) failed: {1}", uri, e);
      return Optional.empty();
    }
  }

  /** Find JAR URI for the specified Maven coordinates. */
  URI findURI(String group, String artifact, String version) {
    group = group.replace('.', '/');
    var jar = String.join("-", artifact, version + ".jar");
    var repository = "http://central.maven.org/maven2";
    return URI.create(String.join("/", repository, group, artifact, version, jar));
  }
}
