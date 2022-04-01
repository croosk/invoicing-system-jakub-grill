package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.futurecollars.invoicing.model.Invoice;

public class JsonService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public JsonService() {
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String convertToString(Invoice invoice) {
    try {
      return objectMapper.writeValueAsString(invoice);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Failed to convert string to JSON", exception);
    }
  }

  public Invoice convertToObject(String objectAsString) {
    try {
      return objectMapper.readValue(objectAsString, Invoice.class);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Failed to parse JSON", exception);
    }
  }
}
