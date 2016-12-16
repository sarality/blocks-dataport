package com.sarality.dataport.file;

import android.os.Environment;

import java.io.File;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Data object representing a File
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileInfo {
  private final String fileName;
  private final String directoryPath;

  private FileInfo(String fileName, String directoryPath) {
    this.fileName = fileName;
    this.directoryPath = directoryPath;
  }

  public FileInfo(String filePath) {
    this.fileName = new File(filePath).getName();
    this.directoryPath = new File(filePath).getParent();
  }

  public static FileInfo downloadsFile(String fileName, boolean appendTimestamp) {
    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    return new FileInfo(generateFileName(fileName, appendTimestamp), directory.getPath());
  }

  private static String generateFileName(String fileName, boolean appendTimestamp) {
    if (!appendTimestamp) {
      return fileName;
    }
    DateTime now = DateTime.now(TimeZone.getDefault());
    String extension = "";
    String errorFileName = fileName;

    int extensionIndex = fileName.lastIndexOf('.');
    if (extensionIndex > 0) {
      extension = fileName.substring(extensionIndex);
      errorFileName = fileName.substring(0, extensionIndex - 1);
    }

    return errorFileName + now.format("_YYYYMMDD_hhmmss") + extension;
  }

  public String getFileName() {
    return fileName;
  }

  public String getDirectoryPath() {
    return directoryPath;
  }
}
