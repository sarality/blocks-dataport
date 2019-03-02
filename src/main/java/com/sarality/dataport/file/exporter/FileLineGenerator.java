package com.sarality.dataport.file.exporter;

/**
 * Generates a Line in a File.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineGenerator<T> {

  /**
   * initialise the line generator
   */
  void init();

  /**
   * @return Header Line for the File
   */
  String generateHeader();

  /**
   * Returns the line for a file as a String
   * @param data Data for which the line is generated.
   * @return Line as a String
   */
  String generateLine(T data);
}
