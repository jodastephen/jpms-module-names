var junit5 = generator.add("JUnit 5")

junit5.homepage = "http://junit.org/junit5"
junit5.description = "The new major version of the programmer-friendly testing framework for Java 8 and beyond."

junit5.scan("org/junit")

junit5.add("org.apiguardian", "apiguardian-api", "1.0.0")
junit5.add("org.opentest4j", "opentest4j", "1.0.0")
