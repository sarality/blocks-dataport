package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.Delimiter;
import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Delimited Line based on FormData generated for a DataObject
 *
 * @author abhideep@ (Abhideep Singh)
 */
class FormDataLineGenerator<T> implements FileLineGenerator<T> {

  private final List<FormField> fieldList;
  private final FormDataConverter<T> dataConverter;
  private final List<FormDataTransformer> transformerList = new ArrayList<>();
  private final Delimiter delimiter;

  FormDataLineGenerator(List<FormField> fieldList, FormDataConverter<T> dataConverter,
      List<FormDataTransformer> transformerList, Delimiter delimiter) {
    this.fieldList = fieldList;
    this.dataConverter = dataConverter;
    if (transformerList != null) {
      this.transformerList.addAll(transformerList);
    }
    this.delimiter = delimiter;
  }

  @Override
  public void init() {
    for (FormDataTransformer transformer : transformerList) {
      transformer.init();
      // TODO (@satya): add the form fields to the fields list
    }
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
    FormData formData = convert(data);
    StringBuilder builder = new StringBuilder();
    int ctr = 0;
    for (FormField field : fieldList) {
      String value = formData.getString(field, true);
      if (ctr > 0) {
        builder.append(delimiter.getStringValue());
      }
      if (value != null) {
        builder.append(value);
      }
      ctr++;
    }
    return builder.toString();
  }

  private FormData convert(T data) {
    FormData formData = dataConverter.generateFormData(data);
    // Any transformations to the column data
    for (FormDataTransformer transformer : transformerList) {
      transformer.transform(formData);
    }
    return formData;
  }

}
