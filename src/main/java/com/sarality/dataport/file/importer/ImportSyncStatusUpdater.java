package com.sarality.dataport.file.importer;

import android.content.ContentValues;

import com.sarality.dataport.file.SyncStatusData;
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
  private final E locallyModifiedValue;
  private final E notLocallyModifiedValue;

  public ImportSyncStatusUpdater(Table<T> table,
      Column idColumn,
      Column globalIdColumn,
      Column globalVersionColumn,
      Column locallyModifiedColumn,
      E locallyModifiedValue,
      E notLocallyModifiedValue) {
    this.table = table;
    this.idColumn = idColumn;
    this.globalIdColumn = globalIdColumn;
    this.globalVersionColumn = globalVersionColumn;
    this.locallyModifiedColumn = locallyModifiedColumn;
    this.locallyModifiedValue = locallyModifiedValue;
    this.notLocallyModifiedValue = notLocallyModifiedValue;
  }

  void updateStatus(Long entityId, SyncStatusData<E> data) {

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

  E getLocallyModifiedValue() {
    return locallyModifiedValue;
  }

  E getNotLocallyModifiedValue() {
    return notLocallyModifiedValue;
  }

}
