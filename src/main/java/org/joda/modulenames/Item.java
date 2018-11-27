package org.joda.modulenames;

import java.util.List;

/** Raw data item. */
class Item {

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
  Item(String csvLine) {
    this.line = csvLine;
    var values = csvLine.split(",");
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
  public String toString() {
    return line;
  }

  private static String blankIfDash(String value) {
    return "-".equals(value) ? "" : value;
  }
}
