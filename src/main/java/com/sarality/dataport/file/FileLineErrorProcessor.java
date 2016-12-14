package com.sarality.dataport.file;

import com.sarality.error.ErrorCode;

import java.util.List;

/**
 * Interface for classes that process an error with the import for a line
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface FileLineErrorProcessor {

  void processErrors(String line, String[] values, List<ErrorCode> errorCodeList);

  void processParseErrors(String line, String[] values, List<String> fieldList);
}
