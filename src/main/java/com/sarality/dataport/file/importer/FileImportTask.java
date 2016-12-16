package com.sarality.dataport.file.importer;

import com.sarality.dataport.file.Delimiter;
import com.sarality.dataport.file.FileInfo;
import com.sarality.dataport.file.InputFileReader;
import com.sarality.dataport.file.OutputFileWriter;
import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;
import com.sarality.task.Task;
import com.sarality.task.TaskProgressPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Import Data provided by the tab or comma separated file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileImportTask<T> implements Task<FileInfo, FileImportProgress, FileImportStatus> {
  private static final Logger logger = LoggerFactory.getLogger(FileImportTask.class);

  private final FileLineParser<T> lineParser;
  private final FileLineDataProcessor<T> dataProcessor;
  private final FileInfo errorFileInfo;
  private final Delimiter delimiter;

  public FileImportTask(List<FormField> fieldList, FormDataConverter<T> dataConverter,
      FileLineDataProcessor<T> dataProcessor, FileInfo errorFileInfo, Delimiter delimiter) {
    this.lineParser = new FormDataFileLineParser<>(fieldList, dataConverter);
    this.dataProcessor = dataProcessor;
    this.errorFileInfo = errorFileInfo;
    this.delimiter = delimiter;
  }

  private FileImportStatus importFile(FileInfo inputFile, TaskProgressPublisher<FileImportProgress> progressPublisher) {
    // First get a count for all the lines
    int numLines = 0;
    InputFileReader reader = new InputFileReader(inputFile);
    try {
      reader.open();
      while (reader.readLine() != null) {
        numLines++;
      }
      numLines--;
    } catch (IOException e) {
      logger.error("Error counting lines in input file " + inputFile.getDirectoryPath() + "/"
          + inputFile.getFileName(), e);
      return new FileImportStatus(inputFile.getFileName(), inputFile.getDirectoryPath(),
          errorFileInfo.getFileName(), errorFileInfo.getDirectoryPath(), false, e.getMessage(), numLines, 0, 0, 0);
    } finally {
      try {
        reader.close();
      } catch (IOException ioe) {
        logger.error("Error closing input file while counting total number of lines in the file" , ioe);
        // DO nothing - as original status will be returned anyway.
      }
    }

    OutputFileWriter writer = new OutputFileWriter(errorFileInfo);
    ErrorFileWriter errorWriter = new ErrorFileWriter(writer, delimiter);
    try {
      reader.open();
      writer.open();
    } catch (IOException e) {
      logger.error("Error opening input file " + inputFile.getDirectoryPath() + "/"
          + inputFile.getFileName() + " and opening error file " + errorFileInfo.getDirectoryPath() + "/"
          + errorFileInfo.getFileName(), e);
      try {
        reader.close();
      } catch (IOException ioe) {
        logger.error("Error closing input reader" , ioe);
        // DO nothing - as original status will be returned anyway.
      }
      try {
        writer.close();
      } catch (IOException ioe) {
        logger.error("Error closing error output writer" , ioe);
        // DO nothing - as original status will be returned anyway.
      }
      return new FileImportStatus(inputFile.getFileName(), inputFile.getDirectoryPath(),
          errorFileInfo.getFileName(), errorFileInfo.getDirectoryPath(), false, e.getMessage(), numLines, 0, 0, 0);
    }

    boolean hasMoreLines = true;
    int ctr = 0;
    int numErrors = 0;
    int numSuccesses = 0;
    while (hasMoreLines) {
      String line;
      try {
        line = reader.readLine();
      } catch (IOException e) {
        line = null;
        // Do Something there
      }
      if (line == null) {
        hasMoreLines = false;
        continue;
      }

      String[] values = line.split(delimiter.getRegularExpression());
      List<String> rowData = Arrays.asList(values);
      if (ctr == 0) {
        lineParser.initColumns(rowData);
        errorWriter.setHeaders(line);
      } else {
        try {
          T data = lineParser.parse(rowData);
          dataProcessor.processData(data);
          numSuccesses++;
        } catch (ApplicationParseException ape) {
          numErrors++;
          errorWriter.processParseErrors(line, ape);
          progressPublisher.updateProgress(new FileImportProgress(numLines, ctr-1, numSuccesses, numErrors));
        } catch (ApplicationException ae) {
          numErrors++;
          errorWriter.processErrors(line, ae);
          progressPublisher.updateProgress(new FileImportProgress(numLines, ctr-1, numSuccesses, numErrors));
        }
      }
      progressPublisher.updateProgress(new FileImportProgress(numLines, ctr-1, numSuccesses, numErrors));
      ctr++;
    }
    progressPublisher.updateProgress(new FileImportProgress(numLines, ctr-1, numSuccesses, numErrors));

    try {
      reader.close();
    } catch (IOException e) {
      logger.error("Error closing input file " + inputFile.getDirectoryPath() + "/" + inputFile.getFileName(), e);
    }
    try {
      writer.close();
    } catch (IOException e) {
      logger.error("Error closing error file " + errorFileInfo.getDirectoryPath() + "/" + errorFileInfo.getFileName(),
          e);
    }
    return new FileImportStatus(inputFile.getFileName(), inputFile.getDirectoryPath(),
        errorFileInfo.getFileName(), errorFileInfo.getDirectoryPath(), true, null,
        numLines, ctr-1, numSuccesses, numErrors);
  }

  @Override
  public FileImportStatus execute(FileInfo inputfile, TaskProgressPublisher<FileImportProgress> progressPublisher) {
    return importFile(inputfile, progressPublisher);
  }
}
