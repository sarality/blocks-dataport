package com.sarality.dataport.file;

/**
 * Parses a line in a file to a data object.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineParser<T> {

  void initColumns(String line, String[] values);

  T parse(String line, String[] values);

  T getData();
}
