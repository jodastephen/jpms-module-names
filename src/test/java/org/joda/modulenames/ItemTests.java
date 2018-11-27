package org.joda.modulenames;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

class ItemTests {

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
}
