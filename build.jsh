//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open PRINTING

println("Compiling Builder...")

/open builder/Builder.java

println("Building...")

Builder builder = new Builder()

// Joda
builder.add(Builder.Item.forModule("org.joda.time").group("joda-time").artifact("joda-time")).project("Joda-Time").homepage("http://www.joda.org/joda-time")
builder.add("org.joda", "joda-beans")
builder.add("org.joda", "joda-convert")

// ASM
builder.add("org.ow2.asm", "asm").project("ASM Core").homepage("http://asm.ow2.org")

// Kordamp
builder.add("org.kordamp.bootstrapfx", "bootstrapfx-core").project("BootstrapFX core").homepage("https://github.com/aalmiray/bootstrapfx")

// JUnit
builder.add("org.junit.jupiter", "junit-jupiter-api").project("JUnit Jupiter").homepage("http://junit.org")

// Square
// builder.add("com.squareup", "javapoet").project("Square JavaPoet").homepage("http://github.com/square/javapoet")
builder.add(Builder.Item.forModule("com.squareup.javapoet").group("com.squareup").artifact("javapoet")).project("Square JavaPoet").homepage("http://github.com/square/javapoet")

printf("%d items collected.%n", builder.items.size())

println("Generating files...")

Files.write(Paths.get("generated/modules.md"), builder.toMarkdownLines())
Files.write(Paths.get("generated/modules.csv"), builder.toCsvLines(","))
Files.write(Paths.get("generated/modules.tsv"), builder.toCsvLines("\t"))
Files.write(Paths.get("generated/module-maven.properties"), builder.toModuleLines(it -> it.group + ":" + it.artifact))
Files.write(Paths.get("generated/module-version.properties"), builder.toModuleLines(it -> it.version))

println("Done.")

/exit
