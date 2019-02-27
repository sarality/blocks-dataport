package com.sarality.dataport.file.importer;

import android.text.TextUtils;

import com.sarality.dataport.file.SyncStatusData;
import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;

import java.util.Arrays;
import java.util.List;

/**
 * Processes a Single Line of the Imported File.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ImportFileLineDataProcessor<T> implements FileLineDataProcessor<T> {
  private final ImportDataPersister<T> persister;
  private final FileLineParser syncStatusParser;
  private final ImportSyncStatusUpdater<T, ? extends Enum<?>> syncStatusUpdater;
  private boolean isInitialized;

  public <E extends Enum<E>> ImportFileLineDataProcessor(ImportDataPersister<T> persister,
      FileLineParser<SyncStatusData<E>> syncStatusParser,
      ImportSyncStatusUpdater<T, E> syncStatusUpdater) {
    this.persister = persister;
    this.syncStatusParser = syncStatusParser;
    this.syncStatusUpdater = syncStatusUpdater;
  }

  @SuppressWarnings("unchecked")
  public ImportFileLineDataProcessor(ImportDataPersister<T> persister) {
    this(persister, null, null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void processData(List<String> columnNames, List<String> rowData, T data)
      throws ApplicationParseException, ApplicationException {
    if (data != null) {
      SyncStatusData syncStatusData = null;
      if (syncStatusParser != null && syncStatusUpdater != null) {
        if (!isInitialized) {
          syncStatusParser.initColumns(columnNames);
          isInitialized = true;
        }
        syncStatusData = (SyncStatusData) syncStatusParser.parse(rowData);
        syncStatusData = sanitize(syncStatusData);
      }
      Long id = persister.persistData(data);
      if (id != null && syncStatusData != null) {
        syncStatusUpdater.updateStatus(id, syncStatusData);
      }
    }
  }

  private SyncStatusData sanitize(SyncStatusData data) throws ApplicationParseException {
    if (data == null) {
      return null;
    }
    String globalId = data.getGlobalId();
    Long globalVersion = data.getGlobalVersion();
    Enum enumValue = data.getEnumValue();
    // If no Global Ids are specified and Locally Modified is True or empty, do nothing.
    if (TextUtils.isEmpty(globalId) && globalVersion == null
        && (enumValue == null || enumValue == syncStatusUpdater.getLocallyModifiedValue())) {
      return null;
    }

    // If Global Id and Version are there but no value for LocallyModified, assume it is not modified.
    if (!TextUtils.isEmpty(globalId) && globalVersion != null && enumValue == null) {
      data.setEnumValue(syncStatusUpdater.getNotLocallyModifiedValue());
    }

    if (!TextUtils.isEmpty(globalId) && globalVersion != null && data.getEnumValue() != null) {
      return data;
    }

    throw new ApplicationParseException("SYNC_DATA_MISMATCH",
        Arrays.asList("GLOBAL_ID", "GLOBAL_VERSION", "LOCALLY_MODIFIED"));
  }
}
