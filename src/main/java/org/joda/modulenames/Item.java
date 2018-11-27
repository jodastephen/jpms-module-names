package org.joda.modulenames;

import java.util.List;
import java.util.Objects;

/** Raw data item. */
final class Item implements Comparable<Item> {

  final String line;

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

  // https://github.com/sandermak/modulescanner/blob/master/src/main/java/org/adoptopenjdk/modulescanner/SeparatedValuesPrinter.java
  Item(String line) {
    this.line = line;
    var values = line.split(",");
    if (values.length < 9) {
      throw new IllegalArgumentException(
          "Expected at least 9 values, only got " + values.length + " in: " + line);
    }
    //
    this.mavenGroupId = blankIfDash(values[0]);
    this.mavenArtifactId = blankIfDash(values[1]);
    this.mavenVersion = blankIfDash(values[2]);
    this.mavenGroupColonArtifact = mavenGroupId + ':' + mavenArtifactId;
    //
    this.moduleName = blankIfDash(values[3]);
    this.moduleVersion = blankIfDash(values[4]);
    this.moduleMode = blankIfDash(values[5]);
    this.moduleDependencies = List.of(values[6].split(" \\+ "));
    //
    this.jdepsToolError = blankIfDash(values[7]);
    this.jdepsViolations = List.of(values[8].split(" \\+ "));
  }

  @Override
  public int compareTo(Item other) {
    return line.compareTo(other.line);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Item)) {
      return false;
    }
    var other = (Item) obj;
    return Objects.equals(this.line, other.line);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(line);
  }

  String name() {
    return moduleName;
  }

  String toMarkdown() {
    var icon = toMarkdownModuleModeIcon();
    return "- " + icon + " `" + moduleName + "` -> " + line;
  }

  String toMarkdownModuleModeIcon() {
    switch (moduleMode) {
      case "automatic":
        return ":cd:";
      case "explicit":
        return ":dvd:";
      default:
        return "?";
    }
  }

  @Override
  public String toString() {
    return line;
  }

  private static String blankIfDash(String value) {
    return "-".equals(value) ? "" : value;
  }
}
