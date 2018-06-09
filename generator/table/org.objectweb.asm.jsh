var asm = generator.add("ASM")

asm.homepage = "http://asm.ow2.org"
asm.description = "ASM is an all purpose Java bytecode manipulation and analysis framework."
asm.group = "org.ow2.asm"

asm.scan("org/ow2/asm/")

asm.rows.removeIf(row -> row.module.startsWith("org.objectweb.asm.all"))
