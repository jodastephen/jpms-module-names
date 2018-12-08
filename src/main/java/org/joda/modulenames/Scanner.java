package org.joda.modulenames;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import static org.joda.modulenames.Utility.loadFromProperties;
import static org.joda.modulenames.Utility.loadString;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.lang.model.SourceVersion;

/** Main scanner program. */
public class Scanner implements AutoCloseable {

  private static final boolean DRY_RUN = false;
  private static final String PREFIX = "reports/";

  public static void main(String... args) {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
    var begin = Instant.now();
    var logger = System.getLogger(Scanner.class.getName());
    logger.log(INFO, "BEGIN");

    var workspace = args.length > 0 ? Path.of(args[0]) : Path.of("target", "workspace");
    logger.log(INFO, "Workspace set to {0}", workspace);

    try (var scanner = new Scanner(logger, workspace)) {
      scanner.scan();
      var s = Duration.between(begin, Instant.now()).getSeconds();
      var clock = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
      logger.log(INFO, "Scanner clock shows {0}", clock);
    } catch (IOException e) {
      logger.log(ERROR, "Scanning failed!", e);
    }
    logger.log(INFO, "END");
  }

  private final System.Logger logger;
  private final Map<String, String> mavenGroupAlias;
  private final Map<String, Item> modules;
  private final Summary summary;
  private final Path lastProcessedObjectPath;
  private final Path modulesProperties;

  private Scanner(System.Logger logger, Path workspace) {
    this.logger = logger;
    this.modules = new TreeMap<>();
    this.summary = new Summary(workspace);
    this.modulesProperties = workspace.resolve("modules.properties");
    this.lastProcessedObjectPath = workspace.resolve("last-processed-object.txt");

    // Load settings...
    this.mavenGroupAlias = loadFromProperties(Path.of("etc", "maven-group-alias.properties"));

    // Load all well-known module items from previous scan runs or bootstrap modules...
    var bootstrapProperties = Path.of("etc", "bootstrap-modules.properties");
    var path = Files.exists(modulesProperties) ? modulesProperties : bootstrapProperties;
    modules.putAll(loadFromProperties(path, Item::new));
    logger.log(INFO, "Starting with {0} well-known modules: {1}", modules.size(), path);

    summary.startedWith = modules.size();
    summary.lastProcessed = loadString(lastProcessedObjectPath);
    summary.startedAfter = summary.lastProcessed;
  }

  @Override
  public void close() {
    if (summary.scanLineCounter == 0) {
      logger.log(INFO, "No single line processed. Nothing to do here.");
      return;
    }
    try {
      // "..."
      Files.createDirectories(summary.workspace);
      Files.writeString(lastProcessedObjectPath, summary.lastProcessed);
      var nameToLineList = new ArrayList<String>();
      var nameToMavenList = new ArrayList<String>();
      var nameToVersionList = new ArrayList<String>();
      for (var item : modules.values()) {
        nameToLineList.add(item.moduleName + '=' + item.line);
        nameToMavenList.add(item.moduleName + '=' + item.mavenGroupColonArtifact);
        nameToVersionList.add(item.moduleName + '=' + item.mavenVersion);
      }
      Files.write(modulesProperties, nameToLineList);
      Files.write(summary.workspace.resolve("module-maven.properties"), nameToMavenList);
      Files.write(summary.workspace.resolve("module-version.properties"), nameToVersionList);

      // "log/summary-<timestamp>.md"
      var logPath = summary.workspace.resolve("log");
      Files.createDirectories(logPath);
      Files.write(logPath.resolve("summary-" + summary.timestamp + ".md"), summary.toMarkdown());
    } catch (IOException e) {
      throw new UncheckedIOException("Closing scanner failed", e);
    }
    // Print summary of the scan
    logger.log(INFO, "Stored {0} modules to {1}", modules.size(), modulesProperties);
    for (var line : summary.toStrings()) {
      logger.log(INFO, "  {0}", line);
    }
  }

  private void scan() throws IOException {
    try (var bucket = new Bucket("java9plusadoption", "us-east-1")) {
      // Prepare and load available keys from the bucket...
      var limit = DRY_RUN ? Integer.MAX_VALUE : 1000;
      var keys = bucket.getKeys(PREFIX, limit, summary.startedAfter);
      if (DRY_RUN || keys.isEmpty()) {
        return;
      }
      summary.firstProcessed = keys.get(0);

      // Process each key by scan all lines within each key...
      for (var key : keys) {
        var counter = ++summary.scanObjectCounter;
        var pointer = String.format("[%d/%d]", counter, keys.size());
        logger.log(INFO, "");
        logger.log(INFO, "{0} -> {1}", pointer, key);
        bucket.processObject(key, this::scanLine);
        logger.log(INFO, "{0} -> {1} modules so far", pointer, modules.size());
        summary.lastProcessed = key;
      }
    }
  }

  private void scanLine(String line) {
    summary.scanLineCounter++;

    if (line.startsWith("groupId,artifactId,version,moduleName,moduleVersion")) {
      logger.log(DEBUG, "Skip header line");
      return;
    }

    var item = new Item(line);
    // No module name available?
    if (item.moduleName.isBlank()) {
      logger.log(DEBUG, "Skip regular Java archive: `{0}`", item.mavenGroupColonArtifact);
      return;
    }

    scanModule(item);
  }

  private void scanModule(Item item) {
    summary.scanModuleCounter++;

    var name = item.moduleName;
    var group = item.mavenGroupId;

    logger.log(DEBUG, "Found {0} module in `{1}`", item.moduleMode, item);

    // Is the (automatic) module name an invalid Java name?
    if (!SourceVersion.isName(name)) {
      logger.log(INFO, "Invalid module name detected: {0}", name);
      if ("explicit".equals(item.moduleMode)) {
        logger.log(WARNING, "Invalid module name `{0}` in an _explicit_ module?!", name);
      }
      summary.suspicious.syntax.add(item);
      return;
    }

    // Is the name of the module already well-known?
    var module = modules.get(name);
    if (module != null) {
      // Is it an identical line, a glitch in the matrix?
      if (module.line.equals(item.line)) {
        logger.log(INFO, "Exact line duplication detected: {0}", item.line);
        return;
      }
      // Is it an update of the Maven version?
      if (module.mavenGroupColonArtifact.equals(item.mavenGroupColonArtifact)) {
        var now = item.mavenVersion;
        var old = module.mavenVersion;
        if (Objects.equals(now, old)) {
          logger.log(INFO, "Version duplication detected: {0}", item.line);
          return;
        }
        logger.log(INFO, "Version of module {0} set to {1} (was={2}) ", name, now, old);
        modules.put(name, item);
        summary.updates.put(name, item);
        return;
      }
      // No, no, an impostor!
      logger.log(INFO, "Impostor of module `{0}` detected: {1}", name, item);
      summary.suspicious.impostors.add(item);
      return;
    }

    // Doesn't the module name starts with its Maven Group ID or an well-know alias thereof?
    if (!name.startsWith(group)) {
      logger.log(INFO, "Module name {0} does not start with its group id: {1}", name, group);
      var alias = mavenGroupAlias.get(group);
      if (alias != null && name.startsWith(alias)) {
        logger.log(INFO, "Module name {0} starts with a group alias {1}", name, alias);
        // fall-through
      } else {
        logger.log(DEBUG, "Module name {0} does not start with a group alias", name);
        summary.suspicious.naming.add(item);
        return;
      }
    }

    //
    // Still here?! Yeah, found either a new unique module name or an updated version.
    //
    logger.log(INFO, "Found new unique {0} module named: `{1}`", item.moduleMode, name);
    modules.put(name, item);
    summary.uniques.put(name, item);
  }
}
