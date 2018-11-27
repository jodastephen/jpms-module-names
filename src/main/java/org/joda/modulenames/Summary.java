package org.joda.modulenames;

import static java.util.Comparator.comparing;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** Single scan run summary. */
class Summary {

  /** Collection of suspicious modules. */
  class Suspicious {
    final Set<Item> impostors = new TreeSet<>();
    final Set<Item> naming = new TreeSet<>();
    final Set<Item> syntax = new TreeSet<>();
  }

  /** Date and time. */
  final String timestamp = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());

  /** Number of scanned lines. */
  long scanLineCounter = 0L;

  /** Number of scanned objects, i.e. csv files. */
  long scanObjectCounter = 0L;

  /** Number of scanned modules. */
  long scanModuleCounter = 0L;

  /** {@code modulescanner-report-2018_11_15_05_33_36.csv} or blank. */
  String lastProcessed = "";

  /** {@code modulescanner-report-2018_11_15_05_33_36.csv} or blank. */
  String firstProcessed = "";

  /** {@code modulescanner-report-2018_11_15_05_33_36.csv} or blank. */
  String startedAfter = "";

  /** Number of modules that were already well-known. */
  int startedWith = 0;

  /** New modules. */
  final Map<String, Item> uniques = new TreeMap<>();

  /** Updated modules. */
  final Map<String, Item> updates = new TreeMap<>();

  /** Suspicious or even plain invalid modules. */
  final Suspicious suspicious = new Suspicious();

  /** Defaults to volatile {@code target/workspace}. */
  final Path workspace;

  Summary(Path workspace) {
    this.workspace = workspace;
  }

  List<String> toStrings() {
    return List.of(
        "Summary of " + timestamp,
        "",
        scanObjectCounter + " objects (`.csv` files) processed",
        scanLineCounter + " lines scanned",
        scanModuleCounter + " module-related events detected",
        "Started with " + startedWith + " well-known modules",
        "Started after file: " + (startedAfter.isBlank() ? "-" : startedAfter),
        "First processed file: " + firstProcessed,
        "Last processed file: " + lastProcessed,
        "",
        uniques.size() + " new modules found",
        updates.size() + " modules updated",
        "",
        suspicious.syntax.size() + " module names were syntactically invalid",
        suspicious.naming.size() + " module names didn't start with the Maven Group or an alias",
        suspicious.impostors.size() + " impostors detected");
  }

  List<String> toMarkdown() {
    var md = new ArrayList<String>();
    md.add("# Scan `" + timestamp + "`");
    md.add("");
    md.add("## Summary");
    md.add("");
    md.add("```");
    md.addAll(toStrings());
    md.add("```");
    toMarkdown(md, "New Unique Modules", uniques);
    toMarkdown(md, "Updated Modules", updates);
    md.add("");
    md.add("## Suspicious Modules");
    md.add("");
    md.add("Modules listed below didn't make it into the `modules.properties` database.");
    toMarkdown(md, "Syntax Error", suspicious.syntax);
    toMarkdown(md, "Impostor", suspicious.impostors);
    toMarkdown(md, "Unexpected Naming", suspicious.naming);
    return md;
  }

  private void toMarkdown(List<String> md, String caption, Map<String, Item> map) {
    md.add("");
    md.add("### " + caption + " (" + map.size() + ")");
    md.add("");
    map.values().forEach(item -> md.add(item.toMarkdown()));
  }

  private void toMarkdown(List<String> md, String caption, Set<Item> items) {
    md.add("");
    md.add("### " + caption + " (" + items.size() + ")");
    md.add("");
    items.stream().sorted(comparing(Item::name)).forEach(item -> md.add(item.toMarkdown()));
  }
}
