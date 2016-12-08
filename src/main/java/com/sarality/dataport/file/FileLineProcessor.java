package com.sarality.dataport.file;

/**
 * Interface for classes that process a line tha is read from a file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineProcessor {

  void processHeader(String line, String[] values);

  void processLine(String line, String[] values);
}
