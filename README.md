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
| | | |
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
| [Ikonli](https://github.com/aalmiray/ikonli) | [org.kordamp.ikonli:ikonli-core](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-core%22) | **org.kordamp.ikonli.core** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-dashicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-dashicons-pack%22) | **org.kordamp.ikonli.dashicons** | v2.1.0 |
|  | [org.kordamp.ikonli:ikonli-devicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-devicons-pack%22) | **org.kordamp.ikonli.devicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-elusive-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-elusive-pack%22) | **org.kordamp.ikonli.elusive** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-feather-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-feather-pack%22) | **org.kordamp.ikonli.feather** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-fontawesome-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-fontawesome-pack%22) | **org.kordamp.ikonli.fontawesome** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-fontawesome5-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-fontawesome5-pack%22) | **org.kordamp.ikonli.fontawesome5** | v2.1.0 |
|  | [org.kordamp.ikonli:ikonli-fontelico-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-fontelico-pack%22) | **org.kordamp.ikonli.fontelico** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-foundation-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-foundation-pack%22) | **org.kordamp.ikonli.foundation** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-hawconsfilled-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-hawconsfilled-pack%22) | **org.kordamp.ikonli.hawconsfilled** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-hawconsstroke-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-hawconsstroke-pack%22) | **org.kordamp.ikonli.hawconsstroke** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-icomoon-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-icomoon-pack%22) | **org.kordamp.ikonli.icomoon** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-ionicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-ionicons-pack%22) | **org.kordamp.ikonli.ionicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-javafx](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-javafx%22) | **org.kordamp.ikonli.javafx** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-ligaturesymbols-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-ligaturesymbols-pack%22) | **org.kordamp.ikonli.ligaturesymbols** | v2.1.0 |
|  | [org.kordamp.ikonli:ikonli-maki-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-maki-pack%22) | **org.kordamp.ikonli.maki** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-maki2-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-maki2-pack%22) | **org.kordamp.ikonli.maki2** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-mapicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-mapicons-pack%22) | **org.kordamp.ikonli.mapicons** | v2.1.0 |
|  | [org.kordamp.ikonli:ikonli-material-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-material-pack%22) | **org.kordamp.ikonli.material** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-materialdesign-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-materialdesign-pack%22) | **org.kordamp.ikonli.materialdesign** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-metrizeicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-metrizeicons-pack%22) | **org.kordamp.ikonli.metrizeicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-octicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-octicons-pack%22) | **org.kordamp.ikonli.octicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-openiconic-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-openiconic-pack%22) | **org.kordamp.ikonli.openiconic** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-runestroicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-runestroicons-pack%22) | **org.kordamp.ikonli.runestroicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-swing](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-swing%22) | **org.kordamp.ikonli.swing** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-typicons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-typicons-pack%22) | **org.kordamp.ikonli.typicons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-weathericons-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-weathericons-pack%22) | **org.kordamp.ikonli.weathericons** | v2.0.1 |
|  | [org.kordamp.ikonli:ikonli-websymbols-pack](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.ikonli%22%20AND%20a%3A%22ikonli-websymbols-pack%22) | **org.kordamp.ikonli.websymbols** | v2.0.1 |
| | | |
| [Jackson (JSON)](https://github.com/FasterXML/jackson) | [com.fasterxml.jackson.core:jackson-core](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jackson-core%22) | [**com.fasterxml.jackson.core**](https://github.com/FasterXML/jackson-core/issues/397) | v2.9.1 |
| [Jackson (JSON)](https://github.com/FasterXML/jackson) | [com.fasterxml.jackson.core:jackson-databind](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jackson-databind%22) | [**com.fasterxml.jackson.databind**](https://github.com/FasterXML/jackson-core/issues/397) | v2.9.1 |
| [Joda-Time](http://www.joda.org/joda-time/) | [joda-time:joda-time](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22joda-time%22%20AND%20a%3A%22joda-time%22) | **org.joda.time** ||
| [Joda-Money](http://www.joda.org/joda-money/) | [org.joda:joda-money](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-money%22) | **org.joda.money** ||
| [Joda-Beans](http://www.joda.org/joda-beans/) | [org.joda:joda-beans](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-beans%22) | [**org.joda.beans**](https://github.com/JodaOrg/joda-beans/issues/175) | v2.0 |
| [Joda-Convert](http://www.joda.org/joda-convert/) | [org.joda:joda-convert](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.joda%22%20AND%20a%3A%22joda-convert%22) | [**org.joda.convert**](https://github.com/JodaOrg/joda-convert/issues/17) | v1.9.1 |
| [JSilhouette](https://github.com/aalmiray/jsilhouette) | [org.kordamp.jsilhouette:jsilhouette-javafx](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.jsilhouette%22%20AND%20a%3A%22jsilhouette-javafx%22) | **org.kordamp.jsilhouette.javafx** | v0.2.2 |
| | | |
| [JUnit Platform](http://junit.org/) | [org.junit.platform:junit-platform-commons](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-commons%22)               | **org.junit.platform.commons**         |  v1.0.2 or v1.1.0-M1 |
|                                     | [org.junit.platform:junit-platform-console](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-console%22)               | **org.junit.platform.console**         |  v1.0.2 or v1.1.0-M1 |
|                                     | [org.junit.platform:junit-platform-launcher](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-launcher%22)             | **org.junit.platform.launcher**        |  v1.0.2 or v1.1.0-M1 |
|                                     | [org.junit.platform:junit-platform-engine](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-engine%22)                 | **org.junit.platform.engine**          |  v1.0.2 or v1.1.0-M1 |
|                                     | [org.junit.platform:junit-platform-runner](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-runner%22)                 | **org.junit.platform.runner**          |  v1.0.2 or v1.1.0-M1 |
|                                     | [org.junit.platform:junit-platform-suite-api](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.platform%22%20AND%20a%3A%22junit-platform-suite-api%22)           | **org.junit.platform.suite.api**       |  v1.0.2 or v1.1.0-M1 |
| [JUnit Jupiter](http://junit.org/)  | [org.junit.jupiter:junit-jupiter-api](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.jupiter%22%20AND%20a%3A%22junit-jupiter-api%22)                           | **org.junit.jupiter.api**              |  v5.0.2 or v5.1.0-M1 |
|                                     | [org.junit.jupiter:junit-jupiter-engine](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.jupiter%22%20AND%20a%3A%22junit-jupiter-api%22)                        | **org.junit.jupiter.engine**           |  v5.0.2 or v5.1.0-M1 |
|                                     | [org.junit.jupiter:junit-jupiter-migrationsupport](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.jupiter%22%20AND%20a%3A%22junit-jupiter-migrationsupport%22) | **org.junit.jupiter.migrationsupport** |  v5.0.2 or v5.1.0-M1 |
|                                     | [org.junit.jupiter:junit-jupiter-params](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.jupiter%22%20AND%20a%3A%22junit-jupiter-params%22)                     | **org.junit.jupiter.params**           |  v5.0.2 or v5.1.0-M1 |
| [JUnit Vintage](http://junit.org/)  | [org.junit.vintage:junit-vintage-engine](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.junit.vintage%22%20AND%20a%3A%22junit-vintage-engine%22)                      | **org.junit.vintage.engine**           | v4.12.2 or v5.1.0-M1 |
| | | |
| [LWJGL](https://www.lwjgl.org) | [org.lwjgl:lwjgl](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl%22) | **org.lwjgl** | 3.1.5 |
|  | [org.lwjgl:lwjgl-assimp](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-assimp%22) | **org.lwjgl.assimp** | 3.1.5 |
|  | [org.lwjgl:lwjgl-bgfx](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-bgfx%22) | **org.lwjgl.bgfx** | 3.1.5 |
|  | [org.lwjgl:lwjgl-egl](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-egl%22) | **org.lwjgl.egl** | 3.1.5 |
|  | [org.lwjgl:lwjgl-glfw](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-glfw%22) | **org.lwjgl.glfw** | 3.1.5 |
|  | [org.lwjgl:lwjgl-jawt](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-jawt%22) | **org.lwjgl.jawt** | 3.1.5 |
|  | [org.lwjgl:lwjgl-jemalloc](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-jemalloc%22) | **org.lwjgl.jemalloc** | 3.1.5 |
|  | [org.lwjgl:lwjgl-lmdb](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-lmdb%22) | **org.lwjgl.lmdb** | 3.1.5 |
|  | [org.lwjgl:lwjgl-lz4](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-lz4%22) | **org.lwjgl.lz4** | 3.1.5 |
|  | [org.lwjgl:lwjgl-nanovg](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-nanovg%22) | **org.lwjgl.nanovg** | 3.1.5 |
|  | [org.lwjgl:lwjgl-nfd](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-nfd%22) | **org.lwjgl.nfd** | 3.1.5 |
|  | [org.lwjgl:lwjgl-nuklear](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-nuklear%22) | **org.lwjgl.nuklear** | 3.1.5 |
|  | [org.lwjgl:lwjgl-odbc](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-odbc%22) | **org.lwjgl.odbc** | 3.1.5 |
|  | [org.lwjgl:lwjgl-openal](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-openal%22) | **org.lwjgl.openal** | 3.1.5 |
|  | [org.lwjgl:lwjgl-opencl](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-opencl%22) | **org.lwjgl.opencl** | 3.1.5 |
|  | [org.lwjgl:lwjgl-opengl](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-opengl%22) | **org.lwjgl.opengl** | 3.1.5 |
|  | [org.lwjgl:lwjgl-opengles](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-opengles%22) | **org.lwjgl.opengles** | 3.1.5 |
|  | [org.lwjgl:lwjgl-openvr](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-openvr%22) | **org.lwjgl.openvr** | 3.1.5 |
|  | [org.lwjgl:lwjgl-ovr](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-ovr%22) | **org.lwjgl.ovr** | 3.1.5 |
|  | [org.lwjgl:lwjgl-par](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-par%22) | **org.lwjgl.par** | 3.1.5 |
|  | [org.lwjgl:lwjgl-remotery](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-remotery%22) | **org.lwjgl.remotery** | 3.1.5 |
|  | [org.lwjgl:lwjgl-rpmalloc](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-rpmalloc%22) | **org.lwjgl.rpmalloc** | 3.1.5 |
|  | [org.lwjgl:lwjgl-sse](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-sse%22) | **org.lwjgl.sse** | 3.1.5 |
|  | [org.lwjgl:lwjgl-stb](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-stb%22) | **org.lwjgl.stb** | 3.1.5 |
|  | [org.lwjgl:lwjgl-tinyexr](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-tinyexr%22) | **org.lwjgl.tinyexr** | 3.1.5 |
|  | [org.lwjgl:lwjgl-tinyfd](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-tinyfd%22) | **org.lwjgl.tinyfd** | 3.1.5 |
|  | [org.lwjgl:lwjgl-tootle](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-tootle%22) | **org.lwjgl.tootle** | 3.1.5 |
|  | [org.lwjgl:lwjgl-vulkan](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-vulkan%22) | **org.lwjgl.vulkan** | 3.1.5 |
|  | [org.lwjgl:lwjgl-xxhash](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-xxhash%22) | **org.lwjgl.xxhash** | 3.1.5 |
|  | [org.lwjgl:lwjgl-yoga](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-yoga%22) | **org.lwjgl.yoga** | 3.1.5 |
|  | [org.lwjgl:lwjgl-zstd](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.lwjgl%22%20AND%20a%3A%22lwjgl-zstd%22) | **org.lwjgl.zstd** | 3.1.5 |
| | | |
| [SLF4J-API](https://www.slf4j.org/) | [org.slf4j:slf4j-api](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.slf4j%22%20AND%20a%3A%22slf4j-api%22) | **org.slf4j** ||
| | | |
| [Spring](https://projects.spring.io/spring-framework/) | [org.springframework:spring-aop](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-aop%22) | **spring.aop** | 5.0.2 |
|  | [org.springframework:spring-aspects](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-aspects%22) | **spring.aspects** | 5.0.2 |
|  | [org.springframework:spring-beans](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-beans%22) | **spring.beans** | 5.0.2 |
|  | [org.springframework:spring-context-indexer](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-context-indexer%22) | **spring.context.indexer** | 5.0.2 |
|  | [org.springframework:spring-context-support](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-context-support%22) | **spring.context.support** | 5.0.2 |
|  | [org.springframework:spring-context](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-context%22) | **spring.context** | 5.0.2 |
|  | [org.springframework:spring-core](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-core%22) | **spring.core** | 5.0.2 |
|  | [org.springframework:spring-expression](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-expression%22) | **spring.expression** | 5.0.2 |
|  | [org.springframework:spring-instrument](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-instrument%22) | **spring.instrument** | 5.0.2 |
|  | [org.springframework:spring-jcl](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-jcl%22) | **spring.jcl** | 5.0.2 |
|  | [org.springframework:spring-jdbc](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-jdbc%22) | **spring.jdbc** | 5.0.2 |
|  | [org.springframework:spring-jms](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-jms%22) | **spring.jms** | 5.0.2 |
|  | [org.springframework:spring-messaging](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-messaging%22) | **spring.messaging** | 5.0.2 |
|  | [org.springframework:spring-orm](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-orm%22) | **spring.orm** | 5.0.2 |
|  | [org.springframework:spring-oxm](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-oxm%22) | **spring.oxm** | 5.0.2 |
|  | [org.springframework:spring-test](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-test%22) | **spring.test** | 5.0.2 |
|  | [org.springframework:spring-tx](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-tx%22) | **spring.tx** | 5.0.2 |
|  | [org.springframework:spring-webflux](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-web%22) | **spring.webflux** | 5.0.2 |
|  | [org.springframework:spring-webmvc](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-webmvc%22) | **spring.webmvc** | 5.0.2 |
|  | [org.springframework:spring-websocket](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.springframework%22%20AND%20a%3A%22spring-websocket%22) | **spring.websocket** | 5.0.2 |
| | | |
| [Square JavaPoet](http://github.com/square/javapoet/) | [com.squareup:javapoet](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.squareup%22%20AND%20a%3A%22javapoet%22) | **com.squareup.javapoet** ||
| | | |


