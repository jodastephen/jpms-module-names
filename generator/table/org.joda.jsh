var joda = generator.add("Joda")

joda.homepage = "http://www.joda.org"
joda.description = "The Joda project provides quality low-level libraries for the Java platform."

joda.add("org.joda", "joda-beans", "2.2")
joda.add("org.joda", "joda-convert", "2.0.1")
joda.add("joda-time", "joda-time", "2.10")

joda.raw("Joda-Money", "org.joda.money", "org.joda", "joda-money")
