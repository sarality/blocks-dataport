package com.sarality.dataport.file;

import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;

/**
 * Converts a Sync Status data object to a FormData and vice versa.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncStatusDataFormConverter<E extends Enum<E>> implements FormDataConverter<SyncStatusData<E>> {
  private final Class<E> enumClass;

  public SyncStatusDataFormConverter(Class<E> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public SyncStatusData<E> generateDomainData(FormData form) {
    SyncStatusData<E> data = new SyncStatusData<>();
    data.setGlobalId(form.getString(SyncStatusFormField.GLOBAL_ID));
    data.setGlobalVersion(form.getLong(SyncStatusFormField.GLOBAL_VERSION));
    data.setEnumValue(form.getEnum(SyncStatusFormField.LOCALLY_MODIFIED, enumClass));
    return data;
  }

  @Override
  public FormData generateFormData(SyncStatusData<E> data) {
    return null;
  }
}
