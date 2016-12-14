package com.sarality.dataport.file;

import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;

/**
 * Counts the total number of lines
 *
 * @author abhideep@ (Abhideep Singh)
 */
class DelimitedFileLineCounter implements FileLineProcessor {
  private int totalItemCount = 0;

  @Override
  public void processHeader(String line, String[] values) {
    // Do Nothing
  }

  @Override
  public void processLine(String line, String[] values) throws ApplicationParseException, ApplicationException {
    totalItemCount++;
  }

  int getTotalItemCount() {
    return totalItemCount;
  }
}
