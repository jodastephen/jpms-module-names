//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open PRINTING

printf("Compiling Generator...%n")

/open generator/Generator.java

printf("Generating tables...%n")

Generator generator = new Generator()
String version = "?"

/open generator/table/org.joda.jsh
/open generator/table/org.junit.jsh
/open generator/table/org.objectweb.asm.jsh
/open generator/table/org.apache.commons.jsh
/open generator/table/org.flywaydb.jsh
/open generator/table/org.jooq.jsh
/open generator/table/org.kordamp.jsh
/open generator/table/org.lwjgl.jsh
/open generator/table/com.google.jsh
/open generator/table/com.fasterxml.jsh
/open generator/table/com.squareup.jsh
/open generator/table/io.netty.jsh
/open generator/table/io.spring.jsh
/open generator/table/org.slf4j.jsh
/open generator/table/org.threeten.jsh

printf("%d table(s) collected.%n", generator.tables.size())

// printf("Generating files...%n")
// int size = generator.dump("generated")
//
// printf("Done. Dumped %d distinct modules.%n", size)

/exit
