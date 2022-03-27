package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        new Company(("$id").repeat(10),
                "ul. Ćwiartki 3/4 $id 66-312 Warszawa, Polska", "Piekarnia Bagietka $id sp. z o.o")
    }

    static product(int id) {
        new InvoiceEntry("Baking and delivering breads and sweet goods $id", BigDecimal.valueOf(id * 500), BigDecimal.valueOf(id * 500 * 0.05), Vat.VAT_5)
    }

    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id), company(id), List.of(product(id)))
    }
}
