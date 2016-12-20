package com.sarality.dataport.file.importer;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the values provided for each Column
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ColumnValueStore {
  private final Map<String, String> columnValueMap = new HashMap<>();

  void addValue(String columnName, String columnValue) {
    this.columnValueMap.put(columnName, columnValue);
  }

  public String getValue(String columnName) {
    return columnValueMap.get(columnName);
  }
}
