package com.sarality.file;

/**
 * Import Data provided by the tab or comma separated file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DelimitedFileImporter {
  private final DelimitedFileReader reader;
  private final FileLineProcessor lineProcessor;

  public DelimitedFileImporter(DelimitedFileReader reader, FileLineProcessor lineProcessor) {
    this.reader = reader;
    this.lineProcessor = lineProcessor;
  }

  public void importFile() {
    reader.readAll(lineProcessor);
  }
}
