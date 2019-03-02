package com.sarality.dataport.file.importer;

import android.text.TextUtils;

import com.sarality.dataport.file.SyncStatusData;
import com.sarality.dataport.file.SyncStatusFormField;
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
    // If none of Global Id, Global version, and Locally modified are specified
    // Then this is a new local entry that has not been synced yet
    if (TextUtils.isEmpty(globalId) && globalVersion == null && enumValue == null) {
      return null;
    }

    // Otherwise all there of GlobalId, Global version, and Locally modified must be set
    if (!TextUtils.isEmpty(globalId) && globalVersion != null && data.getEnumValue() != null) {
      return data;
    }

    throw new ApplicationParseException(Arrays.asList(
        SyncStatusFormField.GLOBAL_ID.name(),
        SyncStatusFormField.GLOBAL_VERSION.name(),
        SyncStatusFormField.LOCALLY_MODIFIED.name()));
  }
}
