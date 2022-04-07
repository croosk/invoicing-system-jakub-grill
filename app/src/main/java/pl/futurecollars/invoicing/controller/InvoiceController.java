package pl.futurecollars.invoicing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @PostMapping
  int addNewInvoice(@RequestBody Invoice invoice) {
    return invoiceService.save(invoice);
  }

  @GetMapping
  public List<Invoice> getAllInvoices() {
    return invoiceService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Invoice> getSingleInvoiceById(@PathVariable int id) {
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateInvoice(@PathVariable int id, @RequestBody Invoice updatedInvoice) {
    return invoiceService.update(id, updatedInvoice)
        .map(invoice -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteInvoice(@PathVariable int id) {
    return invoiceService.delete(id)
        .map(invoice -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }
}
