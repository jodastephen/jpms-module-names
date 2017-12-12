
class CsvGenerator {
    static final def ROW_REGEX = /^\|.*[a-zA-Z0-9]+.*\|$/
    static final def LINK_REGEX = /^.*\[(.*?)\].*$/
    static final def OUTPUT_FORMAT = /"%s","%s","%s","%s"/
    static final def NUM_TOKENS = 5
    static final def NUM_TOKENS_WITH_EMPTY_FIELD = 4

    def generateField(def field) {
        def result = field 

        def matcher = (field =~ LINK_REGEX) 

        if (matcher.matches()) { 
            result = matcher[0][1]
        }

        result.trim().replaceAll(/\*\*/, "")
    }

    // a good entry point for tests
    def generateFields(fields) {
        return fields.collect { generateField(it) }
    }

    def generateLine(def line) {
        def result = ""
        def trimLine = line.trim().replaceAll(/\|\|/, "| |")

        if (trimLine ==~ ROW_REGEX) {
            def fields = trimLine.split(/\|/)

            if (fields.size() == NUM_TOKENS) {
                def (x, f1, f2, f3, f4) = generateFields(fields)
                result = String.format(OUTPUT_FORMAT, f1, f2, f3, f4) 
            } else {
                println "WARN: " + trimLine
            }
        }

        result
    }
    
    def generate(def inFilePath, def outFilePath) {
        def oldLines = new File(inFilePath).readLines()
        def newLines = oldLines.collect { generateLine(it) }.findAll { ! it.isEmpty() }

        new File(outFilePath).withWriter { writer ->
            newLines.each { writer.write("${it}\n") }
        }
    }

    static void main(String[] args) {
        def inFilePath = args[0]
        def outFilePath = args[1]

        def csvGenerator = new CsvGenerator()    
        csvGenerator.generate(inFilePath, outFilePath)
    }
}
