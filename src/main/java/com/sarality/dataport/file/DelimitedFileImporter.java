package com.sarality.dataport.file;

import java.io.IOException;

/**
 * Import Data provided by the tab or comma separated file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DelimitedFileImporter {
  private final DelimitedFileReader inputFileReader;
  private final FileLineProcessor lineProcessor;
  private final DelimitedErrorFileWriter errorFileWriter;

  public DelimitedFileImporter(DelimitedFileReader inputFileReader, FileLineProcessor lineProcessor,
      DelimitedErrorFileWriter errorFileWriter) {
    this.inputFileReader = inputFileReader;
    this.lineProcessor = lineProcessor;
    this.errorFileWriter = errorFileWriter;
  }

  public void importFile() throws IOException {
    errorFileWriter.open();
    inputFileReader.readAll(lineProcessor, errorFileWriter);
    errorFileWriter.close();
  }
}
