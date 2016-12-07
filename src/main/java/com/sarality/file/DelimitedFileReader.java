package com.sarality.file;

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
  private final String separator;
  private final String filePath;

  public DelimitedFileReader(String filePath, String separator) {
    this.separator = separator;
    this.filePath = filePath;
  }

  public void readAll(FileLineProcessor lineProcessor) {
    FileInputStream inputStream = null;
    boolean processedHeaders = false;
    try {
      inputStream = new FileInputStream(filePath);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] rowData = line.split(separator);
        if (!processedHeaders) {
          lineProcessor.processHeader(line, rowData);
          processedHeaders = true;
        } else {
          lineProcessor.processLine(line, rowData);
        }
      }
    } catch (IOException ex) {
      logger.error("Failed to Read Delimited File", ex);
      // handle exception
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
