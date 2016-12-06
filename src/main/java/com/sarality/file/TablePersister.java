package com.sarality.file;

import com.sarality.db.Table;
import com.sarality.db.TableRegistry;

/**
 * Converts File data to FormData and then saves it to a Table.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class TablePersister<T> implements FileLineProcessor {

  private final Table<T> table;
  private final FileLineParser<T> lineParser;

  private TablePersister(Table<T> table, FileLineParser<T> lineParser) {
    this.table = table;
    this.lineParser = lineParser;
  }

  @SuppressWarnings("unchecked")
  public TablePersister(TableRegistry registry, String tableName, FileLineParser<T> lineParser) {
    this((Table<T>) registry.getTable(tableName), lineParser);
  }

  @Override
  public void processLine(String line, String[] values) {
    T data = lineParser.parse(line, values);
    storeData(data);
  }

  private void storeData(T data) {
    try {
      table.open();
      table.create(data);
    } finally {
      table.close();
    }
  }
}
