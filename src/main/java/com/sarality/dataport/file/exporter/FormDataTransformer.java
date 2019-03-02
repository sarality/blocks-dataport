package com.sarality.dataport.file.exporter;

import com.sarality.form.FormData;

/**
 * Transform form fields before generating a line in the export file
 *
 * @author satya (satya puniani)
 */
public interface FormDataTransformer {

  /**
   * initializes the transformer
   */
  void init();

  /**
   * performs the transformation on the form data
   *
   * @param formData: form data representation of the data to be exported
   */
  void transform(FormData formData);

}
