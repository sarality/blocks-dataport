package com.sarality.dataport.file;

/**
 * Data object that contains Sync information for data that has been imported or exported.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncStatusData<E extends Enum<E>> {
  private Long localId;
  private String globalId;
  private Long globalVersion;
  private E enumValue;

  public Long getLocalId() {
    return localId;
  }

  public void setLocalId(Long localId) {
    this.localId = localId;
  }

  public String getGlobalId() {
    return globalId;
  }

  public void setGlobalId(String globalId) {
    this.globalId = globalId;
  }

  public Long getGlobalVersion() {
    return globalVersion;
  }

  public void setGlobalVersion(Long globalVersion) {
    this.globalVersion = globalVersion;
  }

  public E getEnumValue() {
    return enumValue;
  }

  public void setEnumValue(E enumValue) {
    this.enumValue = enumValue;
  }
}
