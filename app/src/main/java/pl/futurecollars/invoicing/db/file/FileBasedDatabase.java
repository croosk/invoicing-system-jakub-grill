package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

  private final Path databasePath;
  private final IdService idService;
  private final FileService fileService;
  private final JsonService jsonService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndIncreament());
      String convert = jsonService.convertToString(invoice);
      fileService.appendLineToFile(databasePath, convert);
      return invoice.getId();
    } catch (IOException expection) {
      throw new RuntimeException("Database failed to save invoice", expection);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return fileService.readAllLines(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(jsonService::convertToObject)
          .findFirst();
    } catch (IOException expection) {
      throw new RuntimeException("Database failed to get invoice with id: " + id, expection);
    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return fileService.readAllLines(databasePath)
          .stream()
          .map(jsonService::convertToObject)
          .collect(Collectors.toList());
    } catch (IOException expection) {
      throw new RuntimeException("Failed to read invoices from file", expection);
    }
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    try {
      List<String> allInvoices = fileService.readAllLines(databasePath);
      List<String> listWithoutInvoiceWithGivenId = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      if (allInvoices.size() == listWithoutInvoiceWithGivenId.size()) {
        throw new IllegalArgumentException("Id " + id + " does not exist");
      }
      updatedInvoice.setId(id);
      String convert = jsonService.convertToString(updatedInvoice);
      listWithoutInvoiceWithGivenId.add(convert);
      fileService.writeLinesToFile(databasePath, listWithoutInvoiceWithGivenId);
    } catch (IOException expection) {
      throw new RuntimeException("Failed to update invoice with id: " + id, expection);
    }
  }

  @Override
  public void delete(int id) {
    try {
      List<String> updatedList = fileService.readAllLines(databasePath)
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());
      fileService.writeLinesToFile(databasePath, updatedList);

    } catch (IOException expection) {
      throw new RuntimeException("Failed to delete invoice with id: " + id, expection);
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");
  }
}
