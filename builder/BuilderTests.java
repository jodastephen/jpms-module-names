import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.module.ModuleDescriptor;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

class BuilderTests {

  @Test
  void explicitModule() {
    assertEquals("ice.cream", describe("explicit-module.jar").name());
  }

  @Test
  void automaticModuleNamePresent() {
    assertEquals("org.apiguardian.api", describe("automatic-module-name-present.jar").name());
  }

  @Test
  void automaticModuleNameNotPresent() {
    String jar = "automatic-module-name-not-present.jar";
    assertThrows(AssertionError.class, () -> describe(jar));
    assertEquals("automatic.module.name.not.present", describe(jar, false).name());
  }

  @Test
  void metaInfNotPresent() {
    String jar = "automatic-metainf-not-present.jar";
    assertThrows(AssertionError.class, () -> describe(jar));
    assertEquals("automatic.metainf.not.present", describe(jar, false).name());
  }

  @Test
  void scanSimplePom() {
    String pom = "automatic-module-name-present.pom";
    Map<String, String> map = new Builder().mapPom(load(pom));
    assertAll(
        "scan " + pom,
        () -> assertEquals("org.apiguardian:apiguardian-api", map.get("name")),
        () -> assertEquals("org.apiguardian", map.get("group")),
        () -> assertEquals("apiguardian-api", map.get("artifact")));
  }

  @Test
  void scanPomWithParent() {
    String pom = "pom-with-parent.pom";
    Map<String, String> map = new Builder().mapPom(load(pom));
    assertAll(
        "scan " + pom,
        () -> assertEquals("BAZ of foo.bar", map.get("name")),
        () -> assertEquals("foo.bar", map.get("group")),
        () -> assertEquals("baz", map.get("artifact")));
  }

  private ModuleDescriptor describe(String jar) {
    return describe(jar, true);
  }

  private ModuleDescriptor describe(String jar, boolean reportFileNameBasedModuleAsEmpty) {
    return new Builder()
        .describeModule(Paths.get(load(jar)), reportFileNameBasedModuleAsEmpty)
        .orElseThrow(AssertionError::new);
  }

  private URI load(String name) {
    try {
      URL resource = getClass().getClassLoader().getResource("test/" + name);
      if (resource == null) {
        throw new IllegalArgumentException("resource not found: " + name);
      }
      return resource.toURI();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
