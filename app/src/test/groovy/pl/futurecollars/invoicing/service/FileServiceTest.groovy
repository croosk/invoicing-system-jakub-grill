package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.service.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FilesServiceTest extends Specification {

    private FileService filesService = new FileService()
    private Path path = File.createTempFile('lines', '.txt').toPath()

    def "line is correctly appended to file"() {
        setup:
        def testLine = "Test line to write"

        expect:
        [] == Files.readAllLines(path)

        when:
        filesService.appendLineToFile(path, testLine)

        then:
        [testLine] == Files.readAllLines(path)

        when:
        filesService.appendLineToFile(path, testLine)

        then:
        [testLine, testLine] == Files.readAllLines(path)
    }

    def "line is correctly written to file"() {
        expect:
        [] == Files.readAllLines(path)

        when:
        filesService.writeToFile(path, "1")

        then:
        ["1"] == Files.readAllLines(path)

        when:
        filesService.writeToFile(path, "2")

        then:
        ["2"] == Files.readAllLines(path)
    }

    def "line are correctly read from file any text"() {
        setup:
        def lines = List.of("line 1", "line 2", "line 3")
        Files.write(path, lines)

        expect:
        lines == filesService.readAllLines(path)
    }

    def "list of lines is correctly written to file"() {
        given:
        def digits = ['1', '2', '3']
        def letters = ['a', 'b', 'c']

        expect:
        [] == Files.readAllLines(path)


        when:
        filesService.writeLinesToFile(path, digits)

        then:
        digits == Files.readAllLines(path)

        when:
        filesService.writeLinesToFile(path, letters)

        then:
        letters == Files.readAllLines(path)
    }

    def "runtime error thrown when try to read from a missing database"() {
        given:
        Files.delete(path)

        when:
        filesService.readAllLines(path)

        then:
        thrown(IOException)
    }

    def "empty file returns empty collection"() {
        expect:
        [] == filesService.readAllLines(path)
    }
}
