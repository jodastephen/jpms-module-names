package org.joda.modulenames;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

class ItemTests {

  private static final List<String> MODULE_LINES =
      List.of(
          "org.ow2.asm,asm,6.0,org.objectweb.asm,-,explicit,-,-,-",
          "org.junit.jupiter,junit-jupiter-api,5.0.0,org.junit.jupiter.api,-,automatic,-,-,-");

  @Test
  void lineIsStoredAsIs() {
    var line = "-" + ",-".repeat(8);
    var item = new Item(line);
    assertEquals(line, item.line);
  }

  @RepeatedTest(20)
  void valueCountUnderflowThrows(RepetitionInfo info) {
    var i = info.getCurrentRepetition();
    var line = "-" + ",-".repeat(i - 1);
    if (i < 9) {
      var e = assertThrows(IllegalArgumentException.class, () -> new Item(line));
      assertEquals("Expected at least 9 values, only got " + i + " in: " + line, e.getMessage());
    } else {
      assertDoesNotThrow(() -> new Item(line));
    }
  }

  @Test
  void moduleModeIcon() {
    assertEquals("?", new Item("-" + ",-".repeat(8)).toMarkdownModuleModeIcon());
    assertEquals(":dvd:", new Item(MODULE_LINES.get(0)).toMarkdownModuleModeIcon());
    assertEquals(":cd:", new Item(MODULE_LINES.get(1)).toMarkdownModuleModeIcon());
  }
}
