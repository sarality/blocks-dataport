package com.sarality.dataport.file;

import com.sarality.task.Task;
import com.sarality.task.TaskProgressPublisher;

import java.io.IOException;
import java.util.List;

/**
 * Import Data provided by the tab or comma separated file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DelimitedFileImporter implements Task<Void, ImportStatus, ImportStatus> {
  private final DelimitedFileReader inputFileReader;
  private final FileLineProcessor lineProcessor;
  private final DelimitedErrorFileWriter errorFileWriter;

  public DelimitedFileImporter(DelimitedFileReader inputFileReader, FileLineProcessor lineProcessor,
      DelimitedErrorFileWriter errorFileWriter) {
    this.inputFileReader = inputFileReader;
    this.lineProcessor = lineProcessor;
    this.errorFileWriter = errorFileWriter;
  }

  private int countLines() throws IOException {
    errorFileWriter.open();
    DelimitedFileLineCounter lineCounter = new DelimitedFileLineCounter();
    inputFileReader.readAll(lineCounter, null, null);
    return lineCounter.getTotalItemCount();
  }

  private ImportStatus importFile(int totalLInes, TaskProgressPublisher<ImportStatus> progressPublisher)
      throws IOException {
    errorFileWriter.open();
    inputFileReader.getStatus().setTotalItemCount(totalLInes);
    inputFileReader.readAll(lineProcessor, errorFileWriter, progressPublisher);
    errorFileWriter.close();
    return inputFileReader.getStatus();
  }

  @Override
  public ImportStatus execute(List<Void> inputList, TaskProgressPublisher<ImportStatus> progressPublisher) {
    int numLines = 0;
    try {
      numLines = countLines();
      return importFile(numLines, progressPublisher);
    } catch (IOException e) {
      return null;
    }
  }
}
