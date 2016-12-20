package com.sarality.dataport.file.importer;

/**
 * Status for an Import Process
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileImportStatus {
  private String fileName;
  private String filePath;

  private String errorFileName;
  private String errorFilePath;

  private boolean importSuccessful;
  private String failureReason;

  private int totalItemCount;
  private int processedCount;
  private int skippedCount;
  private int successCount;
  private int errorCount;

  FileImportStatus(String fileName, String filePath, String errorFileName, String errorFilePath,
      boolean importSuccessful, String failureReason, int totalItemCount, int processedCount, int skippedCount,
      int successCount, int errorCount) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.errorFileName = errorFileName;
    this.errorFilePath = errorFilePath;
    this.importSuccessful = importSuccessful;
    this.failureReason = failureReason;
    this.totalItemCount = totalItemCount;
    this.processedCount = processedCount;
    this.skippedCount = skippedCount;
    this.successCount = successCount;
    this.errorCount = errorCount;
  }

  public String getErrorFileName() {
    return errorFileName;
  }

  public String getErrorFilePath() {
    return errorFilePath;
  }

  public boolean isImportSuccessful() {
    return importSuccessful;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public int getTotalItemCount() {
    return totalItemCount;
  }

  public int getProcessedCount() {
    return processedCount;
  }

  public int getSkippedCount() {
    return skippedCount;
  }

  public int getSuccessCount() {
    return successCount;
  }

  public int getErrorCount() {
    return errorCount;
  }

}
