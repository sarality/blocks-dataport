package com.sarality.file;

/**
 * Interface for classes that process a line tha is read from a file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineProcessor {

  void processLine(String line, String[] values);
}
