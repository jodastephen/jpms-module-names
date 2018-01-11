var apacheCommons = generator.add("Apache Commons")

apacheCommons.homepage = "http://commons.apache.org"
apacheCommons.description = "Apache Commons is an Apache project focused on all aspects of reusable Java components."
apacheCommons.group = "org.apache.commons"

apacheCommons.add("commons-compress", "1.15")
apacheCommons.add("commons-configuration2", "2.2")
apacheCommons.add("commons-lang3", "3.7")

// group = "org.apache.commons"
apacheCommons.raw("Apache Commons Collections v4", "org.apache.commons.collections4", "org.apache.commons", "commons-collections4")
apacheCommons.raw("Apache Commons Crypto", "org.apache.commons.crypto", "org.apache.commons", "commons-crypto")
apacheCommons.raw("Apache Commons CSV", "org.apache.commons.csv", "org.apache.commons", "commons-csv")
apacheCommons.raw("Apache Commons DBCP v2", "org.apache.commons.dbcp2", "org.apache.commons", "commons-dbcp2")

// group = "commons-{component}"
apacheCommons.raw("Apache Commons BeanUtils", "org.apache.commons.beanutils", "commons-beanutils", "commons-beanutils")
apacheCommons.raw("Apache Commons DBUtils", "org.apache.commons.dbutils", "commons-dbutils", "commons-dbutils")
apacheCommons.raw("Apache Commons CLI", "org.apache.commons.cli", "commons-cli", "commons-cli")
apacheCommons.raw("Apache Commons Codec", "org.apache.commons.codec", "commons-codec", "commons-codec")
apacheCommons.raw("Apache Commons Collections v3", "org.apache.commons.collections", "commons-collections", "commons-collections")
apacheCommons.raw("Apache Commons IO", "org.apache.commons.io", "commons-io", "commons-io")
apacheCommons.raw("Apache Commons Lang v2", "org.apache.commons.lang", "commons-lang", "commons-lang")
