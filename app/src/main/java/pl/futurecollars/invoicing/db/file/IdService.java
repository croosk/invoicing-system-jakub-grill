package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.utils.FileService;

public class IdService {

  private final Path idFilePath;
  private final FileService fileService;

  private int nextId;

  public IdService(Path idFilePath, FileService fileService) {
    this.idFilePath = idFilePath;
    this.fileService = fileService;

    try {
      List<String> lines = fileService.readAllLines(idFilePath);
      if (lines.isEmpty()) {
        fileService.writeToFile(idFilePath, "1");
        nextId = 1;
      } else {
        nextId = Integer.parseInt(lines.get(0));
      }
    } catch (IOException exception) {
      throw new RuntimeException("Failed to initialize id database", exception);
    }

  }

  public int getNextIdAndIncreament() {
    try {
      fileService.writeToFile(idFilePath, String.valueOf(nextId + 1));
      return nextId++;
    } catch (IOException exception) {
      throw new RuntimeException("Failed to read id file", exception);
    }
  }

}
