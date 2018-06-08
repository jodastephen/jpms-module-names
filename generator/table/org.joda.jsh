var joda = generator.add("Joda")

joda.homepage = "http://www.joda.org"
joda.description = "The Joda project provides quality low-level libraries for the Java platform."

joda.scan("org/joda/")
joda.raw("Joda-Money", "org.joda.money", "org.joda", "joda-money")

joda.scan("joda-time/")
