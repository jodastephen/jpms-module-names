var joda = generator.add("Joda")

joda.homepage = "http://www.joda.org"
joda.description = "The Joda project provides quality low-level libraries for the Java platform."

joda.add("org.joda", "joda-beans", "2.2")
joda.add("org.joda", "joda-convert", "2.0.1")

joda.raw("Joda-Money", "org.joda.money", "org.joda", "joda-money")
joda.raw("Joda-Time", "org.joda.time", "joda-time", "joda-time")
