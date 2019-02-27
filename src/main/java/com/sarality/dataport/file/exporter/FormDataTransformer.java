package com.sarality.dataport.file.exporter;

import com.sarality.form.FormData;

/**
 * Transform form fields before generating a line in the export file
 *
 * @author satya (satya puniani)
 */
public interface FormDataTransformer {

  void init();

  void transform(FormData formData);

}
