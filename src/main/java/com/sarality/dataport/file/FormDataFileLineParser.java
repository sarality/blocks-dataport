package com.sarality.dataport.file;

import com.sarality.error.ApplicationParseException;
import com.sarality.form.FormData;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  private final Set<String> optionalFieldNameSet = new HashSet<>();
  private final Map<String, FormField> fieldNameMap = new HashMap<>();


  private T value;
  private ErrorCollectingFormData data;

  public FormDataFileLineParser(List<FormField> fieldList, List<FormField> optionalFieldList,
      FormDataConverter<T> dataConverter) {
    this.fieldList = fieldList;
    if (optionalFieldList != null) {
      for (FormField field : optionalFieldList) {
        optionalFieldNameSet.add(field.getName());
      }
    }
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
  public T parse(String line, String[] values) throws ApplicationParseException {
    data = new ErrorCollectingFormData();
    int ctr = 0;
    for (String value : values) {
      String columnName = columnNameList.get(ctr);
      FormField field = fieldNameMap.get(columnName);
      if (value.isEmpty() && optionalFieldNameSet.contains(columnName)) {
        data.addValue(field, null);
      } else {
        data.addValue(field, value);
      }
      ctr++;
    }
    logger.debug("Extracting data object for Form Data {}", data.displayString(fieldList));

    value = dataConverter.generateDomainData(data);
    List<String> parseErrorFieldNameList = data.getParseErrorFieldNames();
    if (parseErrorFieldNameList != null && !parseErrorFieldNameList.isEmpty()) {
      throw new ApplicationParseException(parseErrorFieldNameList);
    }
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
