package com.sarality.dataport.file;

import android.text.TextUtils;

import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;

/**
 * Converts a Sync Status data object to a FormData and vice versa.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncStatusDataFormConverter<E extends Enum<E>> implements FormDataConverter<SyncStatusData<E>> {
  private static final String LOCALLY_MODIFIED_TEXT = "YES";
  private static final String NOT_LOCALLY_MODIFIED_TEXT = "NO";

  private final Class<E> enumClass;
  private final E modifiedEnumValue;
  private final E notModifiedEnumValue;

  public SyncStatusDataFormConverter(Class<E> enumClass, E modifiedEnumValue, E notModifiedEnumValue) {
    this.enumClass = enumClass;
    this.modifiedEnumValue = modifiedEnumValue;
    this.notModifiedEnumValue = notModifiedEnumValue;
  }

  @Override
  public SyncStatusData<E> generateDomainData(FormData form) {
    SyncStatusData<E> data = new SyncStatusData<>();
    data.setGlobalId(form.getString(SyncStatusFormField.GLOBAL_ID));
    data.setGlobalVersion(form.getLong(SyncStatusFormField.GLOBAL_VERSION));
    String locallyModified = form.getString(SyncStatusFormField.LOCALLY_MODIFIED);
    data.setEnumValue(getEnum(locallyModified, data));
    return data;
  }

  @Override
  public FormData generateFormData(SyncStatusData<E> data) {
    FormData form = new FormData();
    form.setString(SyncStatusFormField.GLOBAL_ID, data.getGlobalId());
    form.setLong(SyncStatusFormField.GLOBAL_VERSION, data.getGlobalVersion());
    form.setString(SyncStatusFormField.LOCALLY_MODIFIED, getEnumValue(data.getEnumValue(), data));
    return form;
  }

  private String getEnumValue(E enumValue, SyncStatusData<E> data) {
    if (enumValue == null) {
      return null;
    }
    String globalId = data.getGlobalId();
    Long globalVersion = data.getGlobalVersion();

    // If it hasn't been synced, just return an empty locally modified value as well
    if (TextUtils.isEmpty(globalId) && globalVersion == null) {
      return null;
    }
    // If it is fully synced, no need to mark it is as not modified.
    if (!TextUtils.isEmpty(globalId) && globalVersion != null && enumValue.equals(notModifiedEnumValue)) {
      return null;
    }
    if (enumValue.equals(modifiedEnumValue)) {
      return LOCALLY_MODIFIED_TEXT;
    } else if (enumValue.equals(notModifiedEnumValue)) {
      return NOT_LOCALLY_MODIFIED_TEXT;
    } else {
      return enumValue.name();
    }
  }

  private E getEnum(String value, SyncStatusData<E> data) {
    String globalId = data.getGlobalId();
    Long globalVersion = data.getGlobalVersion();
    if (TextUtils.isEmpty(value)) {
      // Global Id and Version do exist, then it's not modified
      if (!TextUtils.isEmpty(globalId) && globalVersion != null) {
        return notModifiedEnumValue;
      }
      return null;
    } else if (value.equals(LOCALLY_MODIFIED_TEXT)) {
      // If is locally Modified, but not Global Id or Version, then not Sync Data is required
      if (TextUtils.isEmpty(globalId) && globalVersion == null) {
        return null;
      }
      return modifiedEnumValue;
    } else if (value.equals(NOT_LOCALLY_MODIFIED_TEXT)) {
      return notModifiedEnumValue;
    } else {
      return Enum.valueOf(enumClass, value);
    }
  }
}
