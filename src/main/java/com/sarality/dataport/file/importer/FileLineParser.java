package com.sarality.dataport.file.importer;

import com.sarality.error.ApplicationParseException;

import java.util.List;

/**
 * Parses a line in a file to a data object.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineParser<T> {

  void initColumns(List<String> columnNameList);

  T parse(List<String> valueList) throws ApplicationParseException;
}
