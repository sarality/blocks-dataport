package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationParseException;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

  public FormDataFileLineParser(List<FormField> fieldList, FormDataConverter<T> dataConverter) {
    this.fieldList = fieldList;
    this.dataConverter = dataConverter;
    for (FormField field : fieldList) {
      fieldNameMap.put(field.getName(), field);
    }
  }

  @Override
  public void initColumns(List<String> columnNameList) {
    this.columnNameList.addAll(columnNameList);
  }

  @Override
  public T parse(List<String> valueList) throws ApplicationParseException {
    ErrorCollectingFormData formData = new ErrorCollectingFormData();
    int ctr = 0;
    for (String dataValue : valueList) {
      String columnName = columnNameList.get(ctr);
      FormField field = fieldNameMap.get(columnName);
      formData.addValue(field, dataValue);
      ctr++;
    }
    logger.debug("Extracting data object for Form Data {}", formData.displayString(fieldList));

    T data = dataConverter.generateDomainData(formData);
    List<String> parseErrorFieldNameList = formData.getParseErrorFieldNames();
    if (parseErrorFieldNameList != null && !parseErrorFieldNameList.isEmpty()) {
      throw new ApplicationParseException(parseErrorFieldNameList);
    }
    return data;
  }
}
