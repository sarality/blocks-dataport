package com.sarality.file;

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
  private final String separator;
  private final String filePath;

  public DelimitedFileReader(String separator, String filePath) {
    this.separator = separator;
    this.filePath = filePath;
  }

  public void readAll(FileLineProcessor lineProcessor) {
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(filePath);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] rowData = line.split(separator);
        lineProcessor.processLine(line, rowData);
      }
    }
    catch (IOException ex) {
      // handle exception
    }
    finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      }
      catch (IOException e) {
        // handle exception
      }
    }
  }
}
