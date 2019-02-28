package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.Delimiter;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;

import java.util.List;

/**
 * File exporter class that loads, transforms and exports form data to a specified file
 *
 * @author satya (satya puniani)
 */
public abstract class BaseFormFileExporter<T> extends BaseFileExporter<T> {

  protected FileLineGenerator<T> getFileLineGenerator() {
    return new FormDataLineGenerator<>(getFieldList(), getFormDataConverter(),
        getTransformers(), getDelimiter());
  }

  protected abstract Delimiter getDelimiter();

  protected abstract FormDataConverter<T> getFormDataConverter();

  protected abstract List<FormField> getFieldList();

  protected abstract List<FormDataTransformer> getTransformers();

}
