package com.sarality.dataport.file.importer;

/**
 * Progress from the Import Task
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileImportProgress {

  private int totalItemCount;
  private int processedCount;
  private int successCount;
  private int errorCount;
  private int skippedCount;

  public FileImportProgress(int totalItemCount, int processedCount, int successCount, int errorCount,
      int skippedCount) {
    this.totalItemCount = totalItemCount;
    this.processedCount = processedCount;
    this.successCount = successCount;
    this.errorCount = errorCount;
    this.skippedCount = skippedCount;
  }

  public int getTotalItemCount() {
    return totalItemCount;
  }

  public int getProcessedCount() {
    return processedCount;
  }

  public int getSuccessCount() {
    return successCount;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public int getSkippedCount() {
    return skippedCount;
  }
}
