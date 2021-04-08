package com.sarality.dataport.file.importer;

import android.text.TextUtils;

import com.sarality.error.ApplicationException;
import com.sarality.error.ApplicationParseException;
import com.sarality.error.ErrorCode;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormDataTransformer;
import com.sarality.form.FormField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
  private final FormDataTransformer transformer;
  private final FormDataConverter<T> dataConverter;

  private final List<String> columnNameList = new ArrayList<>();
  private final Map<String, FormField> fieldNameMap = new HashMap<>();

  private final List<ColumnValueResolver> columnValueResolverList = new ArrayList<>();
  private final Set<String> resolvedColumnSet = new HashSet<>();

  public FormDataFileLineParser(
      List<FormField> fieldList,
      FormDataConverter<T> dataConverter,
      List<ColumnValueResolver> columnValueResolverList) {
    this(fieldList, null, columnValueResolverList, dataConverter);
  }

  public FormDataFileLineParser(
      List<FormField> fieldList,
      FormDataTransformer transformer,
      List<ColumnValueResolver> columnValueResolverList,
      FormDataConverter<T> dataConverter) {
    this.fieldList = fieldList;
    this.transformer = transformer;
    this.dataConverter = dataConverter;
    for (FormField field : fieldList) {
      fieldNameMap.put(field.getName(), field);
    }
    if (columnValueResolverList != null) {
      this.columnValueResolverList.addAll(columnValueResolverList);
      for (ColumnValueResolver resolver : columnValueResolverList) {
        resolvedColumnSet.addAll(resolver.getColumns());
      }
    }
  }

  @Override
  public void initColumns(List<String> columnNameList) {
    this.columnNameList.addAll(columnNameList);
    for (ColumnValueResolver resolver : columnValueResolverList) {
      resolver.init();
    }
  }

  @Override
  public T parse(List<String> valueList) throws ApplicationParseException {
    ErrorCollectingFormData formData = new ErrorCollectingFormData();
    ColumnValueStore columnValueStore = new ColumnValueStore();
    int ctr = 0;
    int maxCtr = columnNameList.size();
    boolean isEmptyRow = true;
    for (String dataValue : valueList) {
      if (isEmptyRow) {
        isEmptyRow = TextUtils.isEmpty(dataValue);
      }
      // There might be values even after the columns. We want to just ignore them.
      if (ctr < maxCtr) {
        String columnName = columnNameList.get(ctr);
        FormField field = fieldNameMap.get(columnName);

        // Add value to ColumnValueStore
        columnValueStore.addValue(columnName, dataValue);

        // Ignore fields that are not registered with the parser or will be handled by resolvers later
        if (field != null && !resolvedColumnSet.contains(columnName)) {
          formData.addValue(field, dataValue);
        }
      }
      ctr++;
    }

    // Return null object if the row is Empty
    if (isEmptyRow) {
      return null;
    }

    // Transform the form data before the Column values can be resolved
    if (transformer != null) {
      transformer.transform(formData);

      // If it is empty after the transformation, then also return null
      // TODO(abhideep): See how this impacts Sync Data converter
      if (formData.isEmpty()) {
        return null;
      }
    }


    // Resolve data that has been skipped so far, now that we have all the data from all columns
    List<String> resolutionErrorList = new ArrayList<>();
    for (ColumnValueResolver resolver : columnValueResolverList) {
      try {
        resolver.resolve(columnValueStore, formData);
      } catch (ApplicationException e) {
        List<ErrorCode> errorCodes = e.getErrorCodes();
        if (errorCodes != null) {
          for (ErrorCode errorCode : errorCodes) {
            resolutionErrorList.add(errorCode.toString());
          }
        }
      }
    }

    logger.debug("Extracting data object for Form Data {}", formData.displayString(fieldList));

    T data = dataConverter.generateDomainData(formData);


    List<String> parseErrorFieldNameList = formData.getParseErrorFieldNames();

    List<String> combinedErrorList = new ArrayList<>();
    combinedErrorList.addAll(resolutionErrorList);
    combinedErrorList.addAll(parseErrorFieldNameList);

    if (!combinedErrorList.isEmpty()) {
      throw new ApplicationParseException(combinedErrorList);
    }
    return data;
  }
}
