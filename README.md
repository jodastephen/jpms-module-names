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

## Well-known module names

To get the whole process started, I've gathered some module names, based on conversations with the relevant projects.

* Markdown table: [modules.md](generated/modules.md)
* Comma/tab separated values: [modules.csv](generated/modules.csv) / [modules.tsv](generated/modules.tsv)

## Modules published on Maven Central

Generated `.properties` files that contain thousands of unique module names. 

* [module-maven.properties](generated/module-maven.properties) - Module name to Maven Group+Artifact ID map
* [module-version.properties](generated/module-version.properties) - Module name to version map

## Suspicious modules

Modules listed in the _error_ files below are suspicious and need manual correction.
If you find your module listed here, or know an author of one, please [create an issue](https://github.com/jodastephen/jpms-module-names/issues/new) for investigation.

* [error-syntax.txt](error/error-syntax.txt) - Modules with illegal names. E.g. containing Java keywords or dashes (`-`) or...
* [error-javas.txt](error/error-javas.txt) - Module names starting with `java.` or `javax.`...
* [error-simple.txt](error/error-simple.txt) - Module names without a single dot (`.`)...
* [error-duplicates.txt](error/error-duplicates.txt) - Module names that are not unique...
* [error-impostors.txt](error/error-impostors.txt) - Modules that use a well-known name already taken by other modules...
