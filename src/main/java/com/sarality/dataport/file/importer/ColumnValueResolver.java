package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationException;
import com.sarality.form.FormData;

import java.util.Set;

/**
 * Resolve a column value or set of Column Values into values in a Form for data import.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface ColumnValueResolver {

  /**
   * @return Set of Columns that are the source of values that a resolved into FieldValue
   */
  Set<String> getColumns();

  /**
   * Resolves column values into FormData values
   *
   * @param columnValueStore Store with values for the various columns.
   * @param data FormData to be populated
   * @throws ApplicationException
   */
  void resolve(ColumnValueStore columnValueStore, FormData data) throws ApplicationException;

  /**
   * Initialize the resolved with the data that it needs
   */
  void init();
}
