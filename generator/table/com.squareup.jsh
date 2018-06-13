var square = generator.add("Square Open Source")

square.description = "As a company built on open source, here are some of the internally-developed libraries we have contributed back to the community."
square.homepage = "http://square.github.io"
square.group = "com.squareup"

square.scan("com.squareup", uri -> !uri.contains("misk"))
