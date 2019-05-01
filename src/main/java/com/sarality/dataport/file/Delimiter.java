package com.sarality.dataport.file;

/**
 * Enum for Delimiters supported by the Data Portability Service
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum Delimiter {
  TAB("\t", "\\t"),
  COMMA(",", ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"),
  ;

  private String stringValue;
  private String regularExpression;

  Delimiter(String stringValue, String regularExpression) {
    this.stringValue = stringValue;
    this.regularExpression = regularExpression;
  }

  public String getRegularExpression() {
    return regularExpression;
  }

  public String getStringValue() {
    return stringValue;
  }
}
