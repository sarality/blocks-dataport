package com.sarality.dataport.file;

import com.sarality.error.ApplicationParseException;

/**
 * Parses a line in a file to a data object.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineParser<T> {

  void initColumns(String line, String[] values);

  T parse(String line, String[] values) throws ApplicationParseException;

  T getData();
}
