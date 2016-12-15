package com.sarality.dataport.file.exporter;

/**
 * Status of an Export Task that exports data to a File
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileExportStatus {
  private String fileName;
  private String filePath;

  private boolean exportSuccessful;
  private String failureReason;


  private int numItems;
  private int numSuccesses;
  private int numFailures;


  FileExportStatus(String fileName, String filePath, boolean exportSuccessful, String failureReason) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.exportSuccessful = exportSuccessful;
    this.failureReason = failureReason;
  }

  public FileExportStatus(String fileName, String filePath, boolean exportSuccessful, String failureReason,
      int numItems, int numSuccesses, int numFailures) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.exportSuccessful = exportSuccessful;
    this.failureReason = failureReason;
    this.numItems = numItems;
    this.numSuccesses = numSuccesses;
    this.numFailures = numFailures;
  }

  public String getFileName() {
    return fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public boolean isExportSuccessful() {
    return exportSuccessful;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public int getNumItems() {
    return numItems;
  }

  public int getNumSuccesses() {
    return numSuccesses;
  }

  public int getNumFailures() {
    return numFailures;
  }
}
