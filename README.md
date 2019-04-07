# Module names for Java module system

Java SE 9 brought a new feature, the Java Platform Module System (JPMS).
Due to certain constraints in the design, it is important for there to be an agreed naming strategy across open source projects.
My proposed strategy is detailed [here](http://blog.joda.org/2017/04/java-se-9-jpms-module-naming.html).

In summary, the strategy is:

* Module names must be valid Java identifiers! E.g. no Java keywords, no dashes, no... 
* Module names must be reverse-DNS, just like package names, e.g. `org.joda.time`.
* Modules are a group of packages. As such, the module name must be related to the package names.
* Module names are strongly recommended to be the same as the name of the super-package.
* Creating a module with a particular name takes ownership of that package name and everything beneath it.
* As the owner of that namespace, any sub-packages may be grouped into sub-modules as desired so long as no package is in two modules.

## Well-known module names published on Maven Central

To get the whole process started, I've gathered some module names, based on conversations with the relevant projects.
Meanwhile, a _database_ of unique module names is ... generated over at [sormuras/modules](https://github.com/sormuras/modules).

## Follow-up to sormuras/modules

This project is superseded by [https://github.com/sormuras/modules](https://github.com/sormuras/modules).
