//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open PRINTING

println("Compiling Builder...")

/open builder/Builder.java

println("Building...")

Builder builder = new Builder()

// Joda
builder.add(Builder.Line.forModule("org.joda.time").group("joda-time").artifact("joda-time")).project("Joda-Time").homepage("http://www.joda.org/joda-time")
builder.add("org.joda", "joda-beans", "2.0").project("Joda-Beans").homepage("http://www.joda.org/joda-beans")
builder.add("org.joda", "joda-beans").project("Joda-Beans").homepage("http://www.joda.org/joda-beans")

// Kordamp
builder.add("org.kordamp.bootstrapfx", "bootstrapfx-core", "0.2.2").project("BootstrapFX core").homepage("https://github.com/aalmiray/bootstrapfx")

// JUnit
builder.add("org.junit.jupiter", "junit-jupiter-api").project("JUnit Jupiter").homepage("http://junit.org")

// Square
// builder.add("com.squareup", "javapoet").project("Square JavaPoet").homepage("http://github.com/square/javapoet")
builder.add(Builder.Line.forModule("com.squareup.javapoet").group("com.squareup").artifact("javapoet")).project("Square JavaPoet").homepage("http://github.com/square/javapoet")

printf("%d lines collected.%n", builder.lines.size())

Files.write(Paths.get("modules.md"), builder.toMarkdownLines());

/exit
