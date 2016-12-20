package com.sarality.dataport.file.importer;

import com.sarality.form.FormData;
import com.sarality.form.FormField;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * A FormData that stores the names of field with pArse Errors
 *
 * @author abhideep@ (Abhideep Singh)
 */
class ErrorCollectingFormData extends FormData {

  private List<String> parseErrorFieldNames = new ArrayList<>();

  @Override
  public Long getLong(String fieldName) {
    try {
      return super.getLong(fieldName);
    } catch (NumberFormatException nfe) {
      parseErrorFieldNames.add(fieldName);
      return null;
    }
  }

  @Override
  public Integer getInt(String fieldName) {
    try {
      return super.getInt(fieldName);
    } catch (NumberFormatException nfe) {
      parseErrorFieldNames.add(fieldName);
      return null;
    }
  }

  @Override
  public Double getDouble(String fieldName) {
    try {
      return super.getDouble(fieldName);
    } catch (NumberFormatException nfe) {
      parseErrorFieldNames.add(fieldName);
      return null;
    }
  }

  @Override
  public DateTime getDate(String fieldName) {
    DateTime dateTime = super.getDate(fieldName);
    if (dateTime == null) {
      return null;
    }
    if (DateTime.isParseable(dateTime.getRawDateString())) {
      return dateTime;
    } else {
      parseErrorFieldNames.add(fieldName);
      return null;
    }
  }

  @Override
  public <T extends Enum<T>> T getEnum(String fieldName, Class<T> enumClass) {
    try {
      return super.getEnum(fieldName, enumClass);
    } catch (IllegalArgumentException e)  {
      parseErrorFieldNames.add(fieldName);
      return null;
    }
  }

  @Override
  public <T extends Enum<T>> List<T> getEnumList(FormField field, Class<T> enumClass) {
    try {
      return super.getEnumList(field, enumClass);
    } catch (IllegalArgumentException e) {
      parseErrorFieldNames.add(field.getName());
      return null;
    }
  }

  List<String> getParseErrorFieldNames() {
    return parseErrorFieldNames;
  }
}
