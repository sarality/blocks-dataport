package com.sarality.dataport.file;

/**
 * Enum for Delimiters supported by the Data Portability Service
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum Delimiter {
  TAB("\\t", "\t");

  private String regularExpression;
  private String stringValue;

  Delimiter(String regularExpression, String stringValue) {
    this.regularExpression = regularExpression;
    this.stringValue = stringValue;
  }

  public String getRegularExpression() {
    return regularExpression;
  }

  public String getStringValue() {
    return stringValue;
  }
}
