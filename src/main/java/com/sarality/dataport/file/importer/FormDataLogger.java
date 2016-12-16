package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationException;
import com.sarality.form.FormData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs the FormData extracted from a Line of the file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FormDataLogger implements FileLineDataProcessor<FormData> {

  private static final Logger logger = LoggerFactory.getLogger(FormDataLogger.class);

  @Override
  public void processData(FormData data) throws ApplicationException {
    logger.info("Extracted Form Data {}", data);
  }
}
