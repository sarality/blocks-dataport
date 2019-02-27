package com.sarality.dataport.file.importer;

import android.content.ContentValues;

import com.sarality.db.Column;
import com.sarality.db.Table;
import com.sarality.db.content.ContentValueWriter;
import com.sarality.db.query.Query;
import com.sarality.db.query.SimpleQueryBuilder;

/**
 * Marks data as locally modified and stores associated global Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ImportSyncStatusUpdater<T, E extends Enum<E>> {

  private final Table<T> table;

  private final Column idColumn;
  private final Column globalIdColumn;
  private final Column globalVersionColumn;
  private final Column locallyModifiedColumn;

  public ImportSyncStatusUpdater(Table<T> table,
      Column idColumn,
      Column globalIdColumn,
      Column globalVersionColumn,
      Column locallyModifiedColumn) {
    this.table = table;
    this.idColumn = idColumn;
    this.globalIdColumn = globalIdColumn;
    this.globalVersionColumn = globalVersionColumn;
    this.locallyModifiedColumn = locallyModifiedColumn;
  }

  public void updateStatus(Long entityId, ImportMetadata<E> data) {

    ContentValues contentValues = new ContentValues();
    ContentValueWriter writer = new ContentValueWriter(contentValues);
    writer.addString(globalIdColumn, data.getGlobalId());
    writer.addLong(globalVersionColumn, data.getGlobalVersion());
    writer.addEnum(locallyModifiedColumn, data.getEnumValue());

    Query query = new SimpleQueryBuilder().withFilter(idColumn, entityId).build();
    try {
      table.open();
      table.update(contentValues, query);
    } finally {
      table.close();
    }
  }
}
