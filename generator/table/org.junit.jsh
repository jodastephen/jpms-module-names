var junit5 = generator.add("JUnit 5")

junit5.homepage = "http://junit.org/junit5"
junit5.description = "The new major version of the programmer-friendly testing framework for Java 8 and beyond."

version = "1.1.0"
junit5.add("org.junit.platform", "junit-platform-commons", version)
junit5.add("org.junit.platform", "junit-platform-console", version)
junit5.add("org.junit.platform", "junit-platform-launcher", version)
junit5.add("org.junit.platform", "junit-platform-engine", version)
junit5.add("org.junit.platform", "junit-platform-runner", version)
junit5.add("org.junit.platform", "junit-platform-suite-api", version)

version = "5.1.0"
junit5.add("org.junit.jupiter", "junit-jupiter-api", version)
junit5.add("org.junit.jupiter", "junit-jupiter-engine", version)
junit5.add("org.junit.jupiter", "junit-jupiter-migrationsupport", version)
junit5.add("org.junit.jupiter", "junit-jupiter-params", version)

junit5.add("org.junit.vintage", "junit-vintage-engine", version)

junit5.add("org.apiguardian", "apiguardian-api", "1.0.0")
junit5.add("org.opentest4j", "opentest4j", "1.0.0")
