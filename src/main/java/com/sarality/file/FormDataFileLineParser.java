package com.sarality.file;

import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses the line read from a file and generates a Data object for the values.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FormDataFileLineParser<T> implements FileLineParser<T> {

  private static final Logger logger = LoggerFactory.getLogger(FormDataFileLineParser.class);

  private final List<FormField> fieldList;
  private final FormDataConverter<T> dataConverter;
  private final List<String> columnNameList = new ArrayList<>();
  private final Map<String, FormField> fieldNameMap = new HashMap<>();

  private T value;
  private FormData data;

  public FormDataFileLineParser(List<FormField> fieldList, FormDataConverter<T> dataConverter) {
    this.fieldList = fieldList;
    this.dataConverter = dataConverter;
    for (FormField field : fieldList) {
      fieldNameMap.put(field.getName(), field);
    }
  }


  @Override
  public void initColumns(String line, String[] values) {
    columnNameList.addAll(Arrays.asList(values));
  }

  @Override
  public T parse(String line, String[] values) {
    data = new FormData();
    int ctr = 0;
    for (String value : values) {
      String columnName = columnNameList.get(ctr);
      FormField field = fieldNameMap.get(columnName);
      data.addValue(field, value);
    }
    logger.info("Extracting data object for Form Data {}", data);

    value = dataConverter.generateDomainData(data);
    return value;
  }

  @Override
  public T getData() {
    return value;
  }

  FormData getFormData() {
    return data;
  }
}
