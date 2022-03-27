package pl.futurecollars.invoicing.utils

import pl.futurecollars.invoicing.helpers.TestHelpers
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "can convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(88)

        when:
        def invoiceAsString = jsonService.convertToString(invoice)

        and:
        def invoiceFromJson = jsonService.convertToObject(invoiceAsString)

        then:
        invoice == invoiceFromJson
    }
}
