package com.sarality.dataport.file;

import android.support.annotation.Nullable;

import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;
import com.sarality.task.TaskProgressPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Read a Tab or Comma separated File and then processes each line that is read.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DelimitedFileReader {
  public static final Logger logger = LoggerFactory.getLogger(DelimitedFileReader.class);
  private final Delimiter delimiter;
  private final String filePath;

  private final ImportStatus status = new ImportStatus();

  public DelimitedFileReader(String filePath, Delimiter delimiter) {
    this.delimiter = delimiter;
    this.filePath = filePath;
  }

  public ImportStatus getStatus() {
    return status;
  }

  void readAll(FileLineProcessor lineProcessor, @Nullable DelimitedErrorFileWriter errorProcessor,
      @Nullable TaskProgressPublisher<ImportStatus> progressPublisher) throws IOException {
    FileInputStream inputStream = null;
    boolean processedHeaders = false;
    status.resetProcessedCount();
    if (progressPublisher != null) {
      progressPublisher.updateProgress(status);
    }

    try {
      inputStream = new FileInputStream(filePath);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] rowData = line.split(delimiter.getRegularExpression());
        if (!processedHeaders) {
          lineProcessor.processHeader(line, rowData);
          processedHeaders = true;
          if (errorProcessor != null) {
            errorProcessor.setHeaders(line, rowData);
          }
        } else {
          try {
            lineProcessor.processLine(line, rowData);
            status.incrementSuccessCount();
          } catch (ApplicationParseException pe) {
            status.incrementErrorCount();
            if (errorProcessor != null) {
              errorProcessor.processParseErrors(line, rowData, pe.getFieldNames());
            }
          } catch (ApplicationException e) {
            status.incrementErrorCount();
            if (errorProcessor != null) {
              errorProcessor.processErrors(line, rowData, e.getErrorCodes());
            }
          }
        }
        if (progressPublisher != null) {
          progressPublisher.updateProgress(status);
        }
      }
    } catch (IOException ex) {
      logger.error("Failed to Read Delimited File", ex);
      throw ex;
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        logger.error("Failed to Close Delimited File", e);
        // handle exception
      }
    }
  }
}
