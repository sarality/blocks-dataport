package com.sarality.file;

/**
 * Parses a line in a file to a data object.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineParser<T> {

  T parse(String line, String[] values);

  T getData();
}
