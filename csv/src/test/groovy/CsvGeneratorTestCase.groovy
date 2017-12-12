
import org.junit.*
import static org.junit.Assert.*

class CsvGeneratorTestCase {
    def csvGenerator = new CsvGenerator() 

    @Test
    void testGenerateFields_header() {
        def fields = ["", "Project","Maven co-ordinates","JPMS module name","Released version"]

        // test 
        def (x, f1, f2, f3, f4)  = csvGenerator.generateFields(fields)

        assert "" == x
        assert "Project" == f1
        assert "Maven co-ordinates" == f2 
        assert "JPMS module name" == f3
        assert "Released version" == f4
    }

    @Test
    void testGenerateFields_case1() {
        def fields = [""," [BootstrapFX](https://github.com/aalmiray/bootstrapfx) "," [org.kordamp.bootstrapfx:bootstrapfx-core](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.bootstrapfx%22%20AND%20a%3A%22bootstrapfx-core%22) "," **org.kordamp.bootstrapfx.core** "," v0.2.2 "]

        // test 
        def (x, f1, f2, f3, f4)  = csvGenerator.generateFields(fields)

        assert "" == x 
        assert "BootstrapFX" == f1
        assert "org.kordamp.bootstrapfx:bootstrapfx-core" == f2 
        assert "org.kordamp.bootstrapfx.core" == f3
        assert "v0.2.2" == f4
    }

    @Test
    void testGenerateLine_header() {
        def line = "| Project | Maven co-ordinates | JPMS module name | Released version |"

        // test 
        def result = csvGenerator.generateLine(line)

        assert "\"Project\",\"Maven co-ordinates\",\"JPMS module name\",\"Released version\"" == result
    }

    @Test
    void testGenerateLine_one() {
        def line = "| [BootstrapFX](https://github.com/aalmiray/bootstrapfx) | [org.kordamp.bootstrapfx:bootstrapfx-core](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.kordamp.bootstrapfx%22%20AND%20a%3A%22bootstrapfx-core%22) | **org.kordamp.bootstrapfx.core** | v0.2.2 |"

        // test 
        def result = csvGenerator.generateLine(line)

        assert "\"BootstrapFX\",\"org.kordamp.bootstrapfx:bootstrapfx-core\",\"org.kordamp.bootstrapfx.core\",\"v0.2.2\"" == result
    }

    @Test
    void testGenerateLine_two() {
        def line = "| [SLF4J-API](https://www.slf4j.org/) | [org.slf4j:slf4j-api](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.slf4j%22%20AND%20a%3A%22slf4j-api%22) | **org.slf4j** ||"

        // test 
        def result = csvGenerator.generateLine(line)

        assert "\"SLF4J-API\",\"org.slf4j:slf4j-api\",\"org.slf4j\",\"\"" == result
    }
}
