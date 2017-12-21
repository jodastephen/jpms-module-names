import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.module.FindException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
class Builder {

  public static void main(String... args) throws Exception {
    Builder builder = new Builder();

    builder
        .add(Line.forModule("org.joda.time").group("joda-time").artifact("joda-time"))
        .project("Joda-Time")
        .homepage("http://www.joda.org/joda-time");

    builder
        .add("org.joda", "joda-beans", "2.0")
        .project("Joda-Beans")
        .homepage("http://www.joda.org/joda-beans");

    builder
        .add("org.joda", "joda-beans")
        .project("Joda-Beans")
        .homepage("http://www.joda.org/joda-beans");

    // TODO builder.addAll("org.joda", "[The Joda project](http://www.joda.org)")

    Files.write(Paths.get("modules.md"), builder.toMarkdownLines());

    // TODO Files.write(Paths.get("modules.csv"), builder.toCsvLines(","));
  }

  final List<Line> lines;
  final List<Resolver> resolvers;

  public Builder() {
    lines = new ArrayList<>();
    resolvers = List.of(new MvnRepositoryResolver());
  }

  /** Add raw line. */
  public Line add(Line line) {
    lines.add(line);
    return line;
  }

  /** Resolve module and add line. */
  public Line add(String group, String artifact, String version) {
    Line line = resolve(group, artifact, version);
    lines.add(line);
    return line;
  }

  /** Resolve released version, module and add a line. */
  public Line add(String group, String artifact) {
    for (Resolver resolver : resolvers) {
      try {
        String version = resolver.version(group, artifact);
        Line line = resolve(resolver, group, artifact, version);
        lines.add(line);
        return line;
      } catch (NoSuchElementException e) {
        // continue;
      }
    }
    throw new Error("resolving failed: " + group + ":" + artifact);
  }

  public List<String> toMarkdownLines() {
    List<String> list = new ArrayList<>(lines.size() + 16);
    list.add("| Project | JPMS Module Name | Maven Group | Maven Artifact | Released Version |");
    list.add("|---------|------------------|-------------|----------------|------------------|");
    String template = "| %s | %s | %s | %s | %s |";
    String project;
    String module;
    String group;
    String artifact;
    String version;
    Resolver defaultResolver = new MvnRepositoryResolver();
    for (Line line : lines) {
      // project
      project = line.project;
      if (!line.homepage.isEmpty()) {
        project = toMarkdownLink(project, URI.create(line.homepage));
      }
      // module name
      boolean resolved = line.resolver != null;
      module = resolved ? "**" + line.module + "**" : "*" + line.module + "*";
      // maven coordinates
      Resolver resolver = resolved ? line.resolver : defaultResolver;
      group = toMarkdownLink(line.group, resolver.link(line.group));
      artifact = toMarkdownLink(line.artifact, resolver.link(line.group, line.artifact));
      version = line.version;
      if (!version.isEmpty()) {
        version = toMarkdownLink(version, resolver.link(line.group, line.artifact, version));
      }
      // append string
      list.add(String.format(template, project, module, group, artifact, version));
    }
    return list;
  }

  private String toMarkdownLink(String text, URI uri) {
    return String.format("[%s](%s)", text, uri);
  }

  Line resolve(String group, String artifact, String version) {
    for (Resolver resolver : resolvers) {
      try {
        return resolve(resolver, group, artifact, version);
      } catch (NoSuchElementException e) {
        // continue;
      }
    }
    throw new RuntimeException("resolve failed for: " + String.join(":", group, artifact, version));
  }

  Line resolve(Resolver resolver, String group, String artifact, String version) {
    Optional<String> module = Util.scan(resolver.jar(group, artifact, version));
    if (module.isPresent()) {
      return Line.forModule(module.get())
          .group(group)
          .artifact(artifact)
          .version(version)
          .resolved(resolver);
    }
    throw new NoSuchElementException();
  }

  interface Resolver {
    URI link(String group);

    URI link(String group, String artifact);

    URI link(String group, String artifact, String version);

    URI jar(String group, String artifact, String version);

    String version(String group, String artifact);
  }

  static class Line {
    String project = "";
    String homepage = "";
    String group = "";
    String artifact = "";
    String module = "";
    String version = "";
    Resolver resolver = null;

    static Line forModule(String module) {
      Line line = new Line();
      line.module = module;
      return line;
    }

    Line project(String project) {
      this.project = project;
      return this;
    }

    Line homepage(String homepage) {
      this.homepage = homepage;
      return this;
    }

    Line group(String group) {
      this.group = group;
      return this;
    }

    Line artifact(String artifact) {
      this.artifact = artifact;
      return this;
    }

    Line version(String version) {
      this.version = version;
      return this;
    }

    Line resolved(Resolver resolver) {
      this.resolver = resolver;
      return this;
    }
  }

  interface Util {
    /** Simple substring variant using begin and end tag. */
    static String sub(String string, String beginTag, String endTag) {
      return sub(
          string,
          beginTag,
          endTag,
          () -> {
            throw new AssertionError("failed");
          });
    }

    static String sub(
        String string, String beginTag, String endTag, Supplier<String> defaultSupplier) {
      int initialIndex = string.indexOf(beginTag);
      if (initialIndex < 0) {
        return defaultSupplier.get();
      }
      try {
        int beginIndex = initialIndex + beginTag.length();
        int endIndex = string.indexOf(endTag, beginIndex);
        return string.substring(beginIndex, endIndex).trim();
      } catch (Exception e) {
        return defaultSupplier.get();
      }
    }

    /** Read content from specified uri as a string. */
    static Optional<String> read(URI uri) {
      try (InputStream input = uri.toURL().openStream();
          ByteArrayOutputStream output = new ByteArrayOutputStream()) {
        input.transferTo(output);
        return Optional.of(output.toString("UTF-8"));
      } catch (Exception exception) {
        // TODO debug("read({0}) failed: {1}", uri, exception);
        return Optional.empty();
      }
    }

    /** Load content from specified uri into temporary file. */
    static Optional<Path> load(URI uri) {
      try {
        URL url = uri.toURL();
        String[] parts = url.getFile().split("/");
        Path temp = Files.createTempFile("", "-" + parts[parts.length - 1]);
        try (InputStream sourceStream = url.openStream();
            OutputStream targetStream = Files.newOutputStream(temp)) {
          sourceStream.transferTo(targetStream);
        } catch (Exception e) {
          Files.deleteIfExists(temp);
          return Optional.empty();
        }
        return Optional.of(temp);
      } catch (Exception e) {
        return Optional.empty();
      }
    }

    static Optional<String> scan(URI uri) {
      // TODO debug("scanJar({0} -> {1})", uri, value);
      Optional<Path> path = Util.load(uri);
      if (!path.isPresent()) {
        // TODO System.out.printf("scanJar(%s) failed%n", uri);
        return Optional.empty();
      }
      try {
        return scan(path.get(), true);
      } finally {
        try {
          Files.delete(path.get());
        } catch (IOException e) {
          // TODO System.err.println("deleting temp file failed: " + e);
        }
      }
    }

    static Optional<String> scan(Path path, boolean reportFileNameBasedModuleAsEmpty) {
      try {
        Set<ModuleReference> allModules = ModuleFinder.of(path).findAll();
        if (allModules.size() != 1) {
          throw new IllegalStateException("expected to find single module, but got: " + allModules);
        }
        ModuleReference reference = allModules.iterator().next();
        ModuleDescriptor descriptor = reference.descriptor();
        String module = descriptor.name();
        System.out.println(descriptor + " packaged in " + path);
        if (reportFileNameBasedModuleAsEmpty) {
          if (descriptor.isAutomatic()) {
            if (!isAutomaticModuleNameAttributeAvailable(reference)) {
              return Optional.empty();
            }
          }
        }
        return Optional.of(module);
      } catch (FindException e) {
        // TODO debug("scan failed: {0}", e);
        return Optional.empty();
      }
    }

    static boolean isAutomaticModuleNameAttributeAvailable(ModuleReference moduleReference) {
      try (ModuleReader moduleReader = moduleReference.open()) {
        String manifestString =
            moduleReader
                .read("META-INF/MANIFEST.MF")
                .map(StandardCharsets.UTF_8::decode)
                .map(Object::toString)
                .orElse("");
        if (manifestString.contains("Automatic-Module-Name")) {
          return true;
        }
      } catch (IOException e) {
        // TODO debug("reading manifest failed: {0}", e);
      }
      return false;
    }
  }

  class MvnRepositoryResolver implements Resolver {

    URI base(String tail) {
      return URI.create("https://mvnrepository.com/artifact/" + tail);
    }

    @Override
    public URI link(String group) {
      return base(group);
    }

    @Override
    public URI link(String group, String artifact) {
      return base(group + "/" + artifact);
    }

    @Override
    public URI link(String group, String artifact, String version) {
      return base(group + "/" + artifact + "/" + version);
    }

    @Override
    public URI jar(String group, String artifact, String version) {
      URI uri = base(String.join("/", group, artifact, version));
      Optional<String> page = Util.read(uri);
      if (page.isPresent()) {
        Pattern p = Pattern.compile("href=\"([^\"]*)\"", Pattern.DOTALL);
        Matcher m = p.matcher(page.get());
        while (m.find()) {
          String href = m.group(1);
          if (href.endsWith(version + ".jar")) {
            return URI.create(href);
          }
        }
      }
      throw new NoSuchElementException("uri not resolvable: " + uri);
    }

    @Override
    public String version(String group, String artifact) throws NoSuchElementException {
      String page = Util.read(link(group, artifact)).orElseThrow(Error::new);
      if (page.indexOf("vbtn release") > 0) {
        return Util.sub(page, "class=\"vbtn release\">", "<");
      }
      if (page.indexOf("vbtn milestone") > 0) {
        return Util.sub(page, "class=\"vbtn milestone\">", "<");
      }
      if (page.indexOf("vbtn beta") > 0) {
        return Util.sub(page, "class=\"vbtn beta\">", "<");
      }
      if (page.indexOf("vbtn alpha") > 0) {
        return Util.sub(page, "class=\"vbtn alpha\">", "<");
      }
      throw new NoSuchElementException("version not found: " + page);
    }
  }
}
