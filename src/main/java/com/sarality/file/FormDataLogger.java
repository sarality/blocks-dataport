package com.sarality.file;

import com.sarality.form.FormData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs the FormData extracted from a Line of the file.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FormDataLogger implements FileLineProcessor {

  private static final Logger logger = LoggerFactory.getLogger(FormDataLogger.class);

  private final FormDataFileLineParser lineParser;

  public FormDataLogger(FormDataFileLineParser lineParser) {
    this.lineParser = lineParser;
  }

  @Override
  public void processLine(String line, String[] values) {
    lineParser.parse(line, values);
    FormData data = lineParser.getFormData();

    logger.info("Extracting data object for Form Data {}", data);

  }
}
