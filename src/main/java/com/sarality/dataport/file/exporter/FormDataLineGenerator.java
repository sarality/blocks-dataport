package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.Delimiter;
import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import java.util.List;

/**
 * Generates a Delimited Line based on FormData generated for a DataObject
 *
 * @author abhideep@ (Abhideep Singh)
 */
class FormDataLineGenerator<T> implements FileLineGenerator<T> {

  private final List<FormField> fieldList;
  private final FormDataConverter<T> dataConverter;
  private final Delimiter delimiter;

  FormDataLineGenerator(List<FormField> fieldList, FormDataConverter<T> dataConverter,
      Delimiter delimiter) {
    this.fieldList = fieldList;
    this.dataConverter = dataConverter;
    this.delimiter = delimiter;
  }

  @Override
  public String generateHeader() {
    StringBuilder builder = new StringBuilder();
    int ctr = 0;
    for (FormField field : fieldList) {
      String value = field.getName();
      if (ctr > 0) {
        builder.append(delimiter.getStringValue());
      }
      builder.append(value);
      ctr++;
    }
    return builder.toString();
  }

  @Override
  public String generateLine(T data) {
    FormData formData = dataConverter.generateFormData(data);
    StringBuilder builder = new StringBuilder();
    int ctr = 0;
    for (FormField field : fieldList) {
      String value = formData.getString(field);
      if (ctr > 0) {
        builder.append(delimiter.getStringValue());
      }
      builder.append(value);
      ctr++;
    }
    return builder.toString();
  }
}
