# Module names for Java SE 9 JPMS

Java SE 9 brings a new feature, the Java Platform Module System (JPMS).
Due to certain constraints in the design, it is important for there to be an agreed naming strategy across open source projects.
My proposed strategy is detailed [here](http://blog.joda.org/2017/04/java-se-9-jpms-module-naming.html).

In summary, the strategy is:

* Module names must be reverse-DNS, just like package names, e.g. org.joda.time.
* Modules are a group of packages. As such, the module name must be related to the package names.
* Module names are strongly recommended to be the same as the name of the super-package.
* Creating a module with a particular name takes ownership of that package name and everything beneath it.
* As the owner of that namespace, any sub-packages may be grouped into sub-modules as desired so long as no package is in two modules.

To get the whole process started, I've gathered some module names here, based on conversations with the relevant projects:

| Project | Maven co-ordinates | JPMS module name | Released version |
|----|----|----|----|
| [BootstrapFX](https://github.com/aalmiray/bootstrapfx) | [org.kordamp.bootstrapfx:bootstrapfx-core](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.bootstrapfx%22%20AND%20a%3A%22bootstrapfx-core%22) | **org.kordamp.bootstrapfx.core** | v0.2.2 |
| [Commons-BeanUtils](https://commons.apache.org/proper/commons-beanutils/) | [commons-beanutils:commons-beanutils](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-beanutils%22%20AND%20a%3A%22commons-beanutils%22) | **org.apache.commons.beanutils** ||
| [Commons-CLI](https://commons.apache.org/proper/commons-cli/) | [commons-cli:commons-cli](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-cli%22%20AND%20a%3A%22commons-cli%22) | **org.apache.commons.cli** ||
| [Commons-Codec](https://commons.apache.org/proper/commons-codec/) | [commons-codec:commons-codec](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-codec%22%20AND%20a%3A%22commons-codec%22) | **org.apache.commons.codec** ||
| [Commons-Collections v4](https://commons.apache.org/proper/commons-collections/) | [org.apache.commons:commons-collections4](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-collections4%22) | **org.apache.commons.collections4** ||
| [Commons-Collections v3](https://commons.apache.org/proper/commons-collections/) | [commons-collections:commons-collections](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-collections%22%20AND%20a%3A%22commons-collections%22) | **org.apache.commons.collections** ||
| [Commons-Compress](https://commons.apache.org/proper/commons-compress/) | [org.apache.commons:commons-compress](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-compress%22) | **org.apache.commons.compress** | v1.15 |
| [Commons-Crypto](https://commons.apache.org/proper/commons-crypto/) | [org.apache.commons:commons-crypto](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-crypto%22) | **org.apache.commons.crypto** ||
| [Commons-CSV](https://commons.apache.org/proper/commons-csv/) | [org.apache.commons:commons-csv](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-csv%22) | **org.apache.commons.csv** ||
| [Commons-Configuration v2](https://commons.apache.org/proper/commons-configuration/) | [org.apache.commons:commons-configuration2](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-configuration2%22) | **org.apache.commons.configuration2** | v2.2 |
| [Commons-DBCP v2](https://commons.apache.org/proper/commons-dbcp/) | [org.apache.commons:commons-dbcp2](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-dbcp2%22) | **org.apache.commons.dbcp2** ||
| [Commons-DBUtils](https://commons.apache.org/proper/commons-dbutils/) | [commons-dbutils:commons-dbutils](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-dbutils%22%20AND%20a%3A%22commons-dbutils%22) | **org.apache.commons.dbutils** ||
| [Commons-IO](https://commons.apache.org/proper/commons-io/) | [commons-io:commons-io](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-io%22%20AND%20a%3A%22commons-io%22) | **org.apache.commons.io** ||
| [Commons-Lang v3](https://commons.apache.org/proper/commons-lang/) | [org.apache.commons:commons-lang3](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.commons%22%20AND%20a%3A%22commons-lang3%22) | **org.apache.commons.lang3** | v3.6 |
| [Commons-Lang v2](https://commons.apache.org/proper/commons-lang/) | [commons-lang:commons-lang](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22commons-lang%22%20AND%20a%3A%22commons-lang%22) | **org.apache.commons.lang** ||
| | | |
| [Google-Guava](https://github.com/google/guava) | [com.google.guava:guava](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.google.guava%22%20AND%20a%3A%22guava%22) | [**com.google.common**](https://groups.google.com/d/msg/guava-discuss/1I--H7xwwR8/fbvZJCRaBAAJ) | 23.2-jre |
| | | |
| [Joda-Time](http://www.joda.org/joda-time/) | [joda-time:joda-time](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22joda-time%22%20AND%20a%3A%22joda-time%22) | **org.joda.time** ||
| [Joda-Money](http://www.joda.org/joda-money/) | [org.joda:joda-money](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-money%22) | **org.joda.money** ||
| [Joda-Beans](http://www.joda.org/joda-beans/) | [org.joda:joda-beans](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-beans%22) | [**org.joda.beans**](https://github.com/JodaOrg/joda-beans/issues/175) | v2.0 |
| [Joda-Convert](http://www.joda.org/joda-convert/) | [org.joda:joda-convert](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-convert%22) | [**org.joda.convert**](https://github.com/JodaOrg/joda-convert/issues/17) | v1.9.1 |
| | | |
| [SLF4J-API](https://www.slf4j.org/) | [org.slf4j:slf4j-api](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.slf4j%22%20AND%20a%3A%22slf4j-api%22) | **org.slf4j** ||
| [Jackson (JSON)](https://github.com/FasterXML/jackson) | [com.fasterxml.jackson.core:jackson-core](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jackson-core%22) | [**com.fasterxml.jackson.core**](https://github.com/FasterXML/jackson-core/issues/397) | v2.9.1 |
| [Jackson (JSON)](https://github.com/FasterXML/jackson) | [com.fasterxml.jackson.core:jackson-databind](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jackson-databind%22) | [**com.fasterxml.jackson.databind**](https://github.com/FasterXML/jackson-core/issues/397) | v2.9.1 |


