package com.sarality.dataport.file.importer;

import com.sarality.dataport.file.Delimiter;
import com.sarality.dataport.file.OutputFileWriter;
import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;
import com.sarality.error.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Writes the errors during an import to another Delimited File along with Error Reasons
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ErrorFileWriter {

  private static final Logger logger = LoggerFactory.getLogger(ErrorFileWriter.class);

  private final OutputFileWriter writer;
  private final Delimiter delimiter;

  public ErrorFileWriter(OutputFileWriter writer, Delimiter delimiter) {
    this.writer = writer;
    this.delimiter = delimiter;
  }

  void processErrors(String line, ApplicationException ae) {
    List<ErrorCode> errorCodeList = ae.getErrorCodes();
    StringBuilder builder = new StringBuilder(line).append(delimiter.getStringValue());
    if (errorCodeList != null) {
      for (ErrorCode code : errorCodeList) {
        builder.append(code).append(", ");
      }
    }
    writeToFile(builder.toString());
  }

  void processParseErrors(String line, ApplicationParseException ape) {
    List<String> fieldList = ape.getFieldNames();

    StringBuilder builder = new StringBuilder(line).append(delimiter.getStringValue()).append("CANNOT_PARSE[");
    if (fieldList != null) {
      int ctr = 0;
      for (String field : fieldList) {
        if (ctr > 0) {
          builder.append(",");
        }
        builder.append(field);
        ctr++;
      }
    }
    builder.append("]");
    writeToFile(builder.toString());
  }

  void setHeaders(String line) {
    writeToFile(line);
  }

  private void writeToFile(String line) {
    try {
      logger.debug("Writing line to file {} ", line);
      writer.writeToFile(line);
    } catch (IOException e) {
      logger.error("Error writing to Error file", e);
    }
  }
}
