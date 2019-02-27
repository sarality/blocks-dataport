package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationException;

/**
 * Interface for class that store imported data
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface ImportDataPersister<T> {

  Long persistData(T data) throws ApplicationException;
}
