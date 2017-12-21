//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open PRINTING

println("Compiling Builder...")

/open builder/Builder.java

println("Building...")

Builder builder = new Builder()

//
// Insert new item below!
//

// Apache
builder.add(Builder.Item.forModule("org.apache.commons.beanutils").group("commons-beanutils").artifact("commons-beanutils")).project("Commons-BeanUtils")
builder.add(Builder.Item.forModule("org.apache.commons.cli").group("commons-cli").artifact("commons-cli")).project("Commons-CLI")
builder.add(Builder.Item.forModule("org.apache.commons.codec").group("commons-codec").artifact("commons-codec")).project("Commons-Codec")
builder.add(Builder.Item.forModule("org.apache.commons.collections4").group("org.apache.commons").artifact("commons-collections4")).project("Commons-Collections v4")
builder.add(Builder.Item.forModule("org.apache.commons.collections").group("commons-collections").artifact("commons-collections")).project("Commons-Collections v3")
builder.add("org.apache.commons", "commons-compress")
builder.add(Builder.Item.forModule("org.apache.commons.crypto").group("org.apache.commons").artifact("commons-crypto")).project("Commons-Crypto")
builder.add(Builder.Item.forModule("org.apache.commons.csv").group("org.apache.commons").artifact("commons-csv")).project("Commons-CSV")
builder.add("org.apache.commons", "commons-configuration2")
builder.add(Builder.Item.forModule("org.apache.commons.dbcp2").group("org.apache.commons").artifact("commons-dbcp2")).project("Commons-DBCP v2")
builder.add(Builder.Item.forModule("org.apache.commons.dbutils").group("commons-dbutils").artifact("commons-dbutils")).project("Commons-DBUtils")
builder.add(Builder.Item.forModule("org.apache.commons.io").group("commons-io").artifact("commons-io")).project("Commons-IO")
builder.add("org.apache.commons", "commons-lang3")
builder.add(Builder.Item.forModule("org.apache.commons.lang").group("commons-lang").artifact("commons-lang")).project("Commons-Lang v2")

// ASM
builder.add("org.ow2.asm", "asm").project("ASM Core").homepage("http://asm.ow2.org")
builder.add("org.ow2.asm", "asm-analysis")
builder.add("org.ow2.asm", "asm-commons")
builder.add("org.ow2.asm", "asm-tree")
builder.add("org.ow2.asm", "asm-util")
builder.add("org.ow2.asm", "asm-xml")

// Google
builder.add("com.google.guava", "guava")

// Kordamp
builder.add("org.kordamp.bootstrapfx", "bootstrapfx-core").project("BootstrapFX core").homepage("https://github.com/aalmiray/bootstrapfx")
builder.add("org.kordamp.ikonli", "ikonli-core")
builder.add("org.kordamp.ikonli", "ikonli-devicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-elusive-pack")
builder.add("org.kordamp.ikonli", "ikonli-feather-pack")
builder.add("org.kordamp.ikonli", "ikonli-fontawesome-pack")
builder.add("org.kordamp.ikonli", "ikonli-fontelico-pack")
builder.add("org.kordamp.ikonli", "ikonli-foundation-pack")
builder.add("org.kordamp.ikonli", "ikonli-hawconsfilled-pack")
builder.add("org.kordamp.ikonli", "ikonli-hawconsstroke-pack")
builder.add("org.kordamp.ikonli", "ikonli-icomoon-pack")
builder.add("org.kordamp.ikonli", "ikonli-ionicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-javafx")
builder.add("org.kordamp.ikonli", "ikonli-maki-pack")
builder.add("org.kordamp.ikonli", "ikonli-maki2-pack")
builder.add("org.kordamp.ikonli", "ikonli-material-pack")
builder.add("org.kordamp.ikonli", "ikonli-materialdesign-pack")
builder.add("org.kordamp.ikonli", "ikonli-metrizeicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-octicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-openiconic-pack")
builder.add("org.kordamp.ikonli", "ikonli-runestroicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-swing")
builder.add("org.kordamp.ikonli", "ikonli-typicons-pack")
builder.add("org.kordamp.ikonli", "ikonli-weathericons-pack")
builder.add("org.kordamp.ikonli", "ikonli-websymbols-pack")
builder.add("org.kordamp.jsilhouette", "jsilhouette-javafx")

// Faster XML
builder.add("com.fasterxml.jackson.core", "jackson-core")
builder.add("com.fasterxml.jackson.core", "jackson-databind")

// Joda
builder.add(Builder.Item.forModule("org.joda.time").group("joda-time").artifact("joda-time")).project("Joda-Time")
builder.add(Builder.Item.forModule("org.joda.money").group("org.joda").artifact("joda-money")).project("Joda-Money")
builder.add("org.joda", "joda-beans")
builder.add("org.joda", "joda-convert")

// JUnit "5"
builder.add("org.junit.platform", "junit-platform-commons")
builder.add("org.junit.platform", "junit-platform-console")
builder.add("org.junit.platform", "junit-platform-launcher")
builder.add("org.junit.platform", "junit-platform-engine")
builder.add("org.junit.platform", "junit-platform-runner")
builder.add("org.junit.platform", "junit-platform-suite-api")
builder.add("org.junit.jupiter", "junit-jupiter-api")
builder.add("org.junit.jupiter", "junit-jupiter-engine")
builder.add("org.junit.jupiter", "junit-jupiter-migrationsupport")
builder.add("org.junit.jupiter", "junit-jupiter-params")
builder.add("org.junit.vintage", "junit-vintage-engine")

// LWJGL
builder.add("org.lwjgl", "lwjgl")
builder.add("org.lwjgl", "lwjgl-assimp")
builder.add("org.lwjgl", "lwjgl-bgfx")
builder.add("org.lwjgl", "lwjgl-egl")
builder.add("org.lwjgl", "lwjgl-glfw")
builder.add("org.lwjgl", "lwjgl-jawt")
builder.add("org.lwjgl", "lwjgl-jemalloc")
builder.add("org.lwjgl", "lwjgl-lmdb")
builder.add("org.lwjgl", "lwjgl-lz4")
builder.add("org.lwjgl", "lwjgl-nanovg")
builder.add("org.lwjgl", "lwjgl-nfd")
builder.add("org.lwjgl", "lwjgl-nuklear")
builder.add("org.lwjgl", "lwjgl-odbc")
builder.add("org.lwjgl", "lwjgl-openal")
builder.add("org.lwjgl", "lwjgl-opencl")
builder.add("org.lwjgl", "lwjgl-opengl")
builder.add("org.lwjgl", "lwjgl-opengles")
builder.add("org.lwjgl", "lwjgl-openvr")
builder.add("org.lwjgl", "lwjgl-ovr")
builder.add("org.lwjgl", "lwjgl-par")
builder.add("org.lwjgl", "lwjgl-remotery")
builder.add("org.lwjgl", "lwjgl-rpmalloc")
builder.add("org.lwjgl", "lwjgl-sse")
builder.add("org.lwjgl", "lwjgl-stb")
builder.add("org.lwjgl", "lwjgl-tinyexr")
builder.add("org.lwjgl", "lwjgl-tinyfd")
builder.add("org.lwjgl", "lwjgl-tootle")
builder.add("org.lwjgl", "lwjgl-vulkan")
builder.add("org.lwjgl", "lwjgl-xxhash")
builder.add("org.lwjgl", "lwjgl-yoga")
builder.add("org.lwjgl", "lwjgl-zstd")

// Netty
builder.add("io.netty", "netty-buffer")
builder.add("io.netty", "netty-codec")
builder.add("io.netty", "netty-codec-dns")
builder.add("io.netty", "netty-codec-haproxy")
builder.add("io.netty", "netty-codec-http")
builder.add("io.netty", "netty-codec-http2")
builder.add("io.netty", "netty-codec-memcache")
builder.add("io.netty", "netty-codec-mqtt")
builder.add("io.netty", "netty-codec-redis")
builder.add("io.netty", "netty-codec-smtp")
builder.add("io.netty", "netty-codec-socks")
builder.add("io.netty", "netty-codec-stomp")
builder.add("io.netty", "netty-codec-xml")
builder.add("io.netty", "netty-common")
builder.add("io.netty", "netty-handler")
builder.add("io.netty", "netty-handler-proxy")
builder.add("io.netty", "netty-resolver")
builder.add("io.netty", "netty-resolver-dns")
builder.add("io.netty", "netty-transport")
builder.add("io.netty", "netty-transport-native-epoll")
builder.add("io.netty", "netty-transport-native-kqueue")
builder.add("io.netty", "netty-transport-rxtx")
builder.add("io.netty", "netty-transport-sctp")
builder.add("io.netty", "netty-transport-udt")
builder.add("io.netty", "netty-transport-native-unix-common")

// SLF4J
builder.add("org.slf4j", "slf4j-api", "1.8.0-beta0")

// Spring
builder.add("org.springframework", "spring-aop")
builder.add("org.springframework", "spring-aspects")
builder.add("org.springframework", "spring-beans")
builder.add("org.springframework", "spring-context-indexer")
builder.add("org.springframework", "spring-context-support")
builder.add("org.springframework", "spring-context")
builder.add("org.springframework", "spring-core")
builder.add("org.springframework", "spring-expression")
builder.add("org.springframework", "spring-instrument")
builder.add("org.springframework", "spring-jcl")
builder.add("org.springframework", "spring-jdbc")
builder.add("org.springframework", "spring-jms")
builder.add("org.springframework", "spring-messaging")
builder.add("org.springframework", "spring-orm")
builder.add("org.springframework", "spring-oxm")
builder.add("org.springframework", "spring-test")
builder.add("org.springframework", "spring-tx")
builder.add("org.springframework", "spring-webflux")
builder.add("org.springframework", "spring-webmvc")
builder.add("org.springframework", "spring-websocket")

// Square
builder.add(Builder.Item.forModule("com.squareup.javapoet").group("com.squareup").artifact("javapoet")).project("Square JavaPoet").homepage("http://github.com/square/javapoet")
builder.add(Builder.Item.forModule("com.squareup.kotlinpoet").group("com.squareup").artifact("kotlinpoet")).project("Square KotlinPoet").homepage("http://github.com/square/kotlinpoet")

//
// Insert new item above!
//

printf("%d items collected.%n", builder.items.size())

println("Generating files...")

Files.write(Paths.get("generated/modules.md"), builder.toMarkdownLines())
Files.write(Paths.get("generated/modules.csv"), builder.toCsvLines(","))
Files.write(Paths.get("generated/modules.tsv"), builder.toCsvLines("\t"))
Files.write(Paths.get("generated/module-maven.properties"), builder.toModuleLines(it -> it.group + ":" + it.artifact))
Files.write(Paths.get("generated/module-version.properties"), builder.toModuleLines(it -> it.version))

println("Done.")

/exit
