package hello.world;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

class HelloWorldTests {

  @Test
  void hi() {}

  @Test
  void opcodeASM6() {
    assertEquals(393216, Opcodes.ASM6);
    assertEquals("org.objectweb.asm@6.1.1", Opcodes.class.getModule().getDescriptor().toNameAndVersion());
  }
}
