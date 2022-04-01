package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice

abstract class DatabaseTest extends Specification {

    abstract Database getDatabaseInstance()

    List<Invoice> invoices = (1..12).collect { invoice(it) }
    Database database = getDatabaseInstance()

    def "should save invoices returning sequential id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect({ database.save(it) })

        then:
        ids == (1..invoices.size()).collect()
        ids.forEach({ assert database.getById(it).isPresent() })
        ids.forEach({ assert database.getById(it).get().getId() == it })
        ids.forEach({ assert database.getById(it).get() == invoices.get(it - 1) })
    }

    def "get by id returns empty optional when there is no invoice with given id"() {
        expect:
        !database.getById(1).isPresent()
    }

    def "get all returns empty collection if there were no invoices"() {
        expect:
        database.getAll().isEmpty()
    }

    def "get all returns all invoices in the database, deleted invoice is not returned"() {
        given:
        invoices.forEach({ database.save(it) })

        expect:
        database.getAll().size() == invoices.size()
        database.getAll().forEach({ assert it == invoices.get(it.getId() - 1) })

        when:
        database.delete(1)

        then:
        database.getAll().size() == invoices.size() - 1
        database.getAll().forEach({ assert it == invoices.get(it.getId() - 1) })
        database.getAll().forEach({ assert it.getId() != 1 })
    }

    def "can delete all invoices"() {
        given:
        invoices.forEach({ database.save(it) })

        when:
        invoices.forEach({ database.delete(it.getId()) })

        then:
        database.getAll().isEmpty()
    }

    def "deleting not existing invoice returns Optional.empty"() {
        expect:
        database.delete(123) == Optional.empty()
    }

    def "update the existing invoice returns old invoice"() {
        given:
        def oldInvoice = invoices.get(0)
        int id = database.save(oldInvoice)

        when:
        def result = database.update(id, invoices.get(1))

        then:
        database.getById(id).get() == invoices.get(1)
        result == Optional.of(oldInvoice)
    }

    def "updating not existing invoice returns Optional.empty"() {
        expect:
        database.update(590, invoices.get(1)) == Optional.empty()
    }
}
