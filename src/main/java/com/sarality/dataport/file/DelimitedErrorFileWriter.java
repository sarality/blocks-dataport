package com.sarality.dataport.file;

import android.os.Environment;

import com.sarality.error.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Writes the errors during an import to another Delimited File along with Error Reasons
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DelimitedErrorFileWriter implements FileLineErrorProcessor {

  private static final Logger logger = LoggerFactory.getLogger(DelimitedErrorFileWriter.class);

  private final String fileName;
  private final Delimiter delimiter;
  private BufferedWriter bufferedWriter = null;

  public DelimitedErrorFileWriter(String fileName, Delimiter delimiter) {
    this.fileName = fileName;
    this.delimiter = delimiter;
  }

  public void open() throws IOException {
    DateTime now = DateTime.now(TimeZone.getDefault());
    String extension = "";
    String errorFileName = fileName;

    int extensionIndex = fileName.lastIndexOf('.');
    if (extensionIndex > 0) {
      extension = fileName.substring(extensionIndex);
      errorFileName = fileName.substring(0, extensionIndex - 1);
    }

    String timeStampedFileName = errorFileName + now.format("_YYYYMMDD_hhmmss") + extension;
    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    bufferedWriter = new BufferedWriter(new FileWriter(
        new File(directory.getPath(), timeStampedFileName)));
  }

  public void close() throws IOException {
    if (bufferedWriter != null) {
      bufferedWriter.close();
    }
  }

  @Override
  public void processErrors(String line, String[] values, List<ErrorCode> errorCodeList) {
    StringBuilder builder = new StringBuilder(line).append(delimiter.getStringValue());
    if (errorCodeList != null) {
      for (ErrorCode code : errorCodeList) {
        builder.append(code).append(", ");
      }
    }
    writeToFile(builder.toString());
  }

  @Override
  public void processParseErrors(String line, String[] values, List<String> fieldList) {
    StringBuilder builder = new StringBuilder(line).append(delimiter).append("CANNOT_PARSE: ");
    if (fieldList != null) {
      for (String field : fieldList) {
        builder.append(field).append(", ");
      }
    }
    writeToFile(builder.toString());
  }

  public void setHeaders(String line, String[] values) {
    writeToFile(line);
  }

  private void writeToFile(String line) {
    try {
      bufferedWriter.newLine();
      logger.warn("Writing line to file {} ", line);
      bufferedWriter.write(line);
      bufferedWriter.flush();
    } catch (IOException e) {
      logger.error("Error writing to Error file", e);
    }
  }
}
