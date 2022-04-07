package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.config.PathsConfiguration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
public class DatabaseConfiguration {

  @Bean
  public IdService idService(FileService fileService) throws IOException {
    Path idFilePath = Files.createTempFile(PathsConfiguration.DATABASE_LOCATION, PathsConfiguration.ID_FILE_PATH);
    return new IdService(idFilePath, fileService);
  }

  @Bean
  public Database fileBasedDatabase(IdService idService, FileService fileService, JsonService jsonService) throws IOException {
    Path databaseFilePath = Files.createTempFile(PathsConfiguration.DATABASE_LOCATION, PathsConfiguration.INVOICES_FILE_PATH);
    return new FileBasedDatabase(databaseFilePath, idService, fileService, jsonService);
  }
}
