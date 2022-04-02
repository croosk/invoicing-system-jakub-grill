package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.service.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {

    private Path nextIdDatabasePath = File.createTempFile('nextId', '.txt').toPath()

    def "if file was empty, starts next id from 1"() {
        given:
        IdService idService = new IdService(nextIdDatabasePath, new FileService())

        expect:
        ['1'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['2'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['3'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['4'] == Files.readAllLines(nextIdDatabasePath)
    }

    def "if file was not empty, starts next id from last number"() {
        given:
        Files.writeString(nextIdDatabasePath, "96")
        IdService idService = new IdService(nextIdDatabasePath, new FileService())

        expect:
        ['96'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['97'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['98'] == Files.readAllLines(nextIdDatabasePath)

        and:
        idService.getNextIdAndIncreament()
        ['99'] == Files.readAllLines(nextIdDatabasePath)
    }
}