package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;

/**
 * Interface for classes that process a line tha is read from a file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineDataProcessor<T> {

  void processData(T data) throws ApplicationException;
}
