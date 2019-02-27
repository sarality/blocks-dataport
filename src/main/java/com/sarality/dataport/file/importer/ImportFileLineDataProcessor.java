package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;

import java.util.List;

/**
 * Processes a Single Line of the Imported File.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ImportFileLineDataProcessor<T> implements FileLineDataProcessor<T> {
  private final ImportDataPersister<T> persister;
  private final FileLineParser importMetadataParser;
  private final ImportSyncStatusUpdater<T, ? extends Enum<?>> syncStatusUpdater;

  public <E extends Enum<E>> ImportFileLineDataProcessor(ImportDataPersister<T> persister,
      FileLineParser<ImportMetadata<E>> importMetadataParser,
      ImportSyncStatusUpdater<T, E> syncStatusUpdater) {
    this.persister = persister;
    this.importMetadataParser = importMetadataParser;
    this.syncStatusUpdater = syncStatusUpdater;
  }

  @SuppressWarnings("unchecked")
  public ImportFileLineDataProcessor(ImportDataPersister<T> persister) {
    this(persister, null, null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void processData(List<String> rowData, T data) throws ApplicationParseException, ApplicationException {
    if (data != null) {
      ImportMetadata importMetadata = null;
      if (importMetadataParser != null) {
        importMetadata = (ImportMetadata) importMetadataParser.parse(rowData);
      }
      Long id = persister.persistData(data);
      if (importMetadata != null) {
        syncStatusUpdater.updateStatus(id, importMetadata);
      }
    }
  }
}
