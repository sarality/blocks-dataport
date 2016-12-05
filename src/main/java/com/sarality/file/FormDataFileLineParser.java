package com.sarality.file;

import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Parses the line read from a file and generates a Data object for the values.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FormDataFileLineParser<T> implements FileLineParser<T> {

  private static final Logger logger = LoggerFactory.getLogger(FormDataFileLineParser.class);

  private final List<FormField> fieldList;
  private final FormDataConverter<T> dataConverter;

  private T value;

  public FormDataFileLineParser(List<FormField> fieldList, FormDataConverter<T> dataConverter) {
    this.fieldList = fieldList;
    this.dataConverter = dataConverter;
  }

  @Override
  public T parse(String line, String[] values) {
    FormData data = new FormData();
    int ctr = 0;
    for (String value : values) {
      FormField field = fieldList.get(ctr);
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
}
