package com.sarality.dataport.file.importer;

/**
 * Data object that contains Sync information for data that has been imported.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ImportMetadata<E extends Enum<E>> {
  private String globalId;
  private Long globalVersion;
  private E enumValue;

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
