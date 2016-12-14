package com.sarality.dataport.file;

/**
 * Status for an Import Process
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ImportStatus {
  private int totalItemCount;
  private int processedCount;
  private int successCount;
  private int errorCount;

  public int getTotalItemCount() {
    return totalItemCount;
  }

  void setTotalItemCount(int totalItemCount) {
    this.totalItemCount = totalItemCount;
  }

  public int getProcessedCount() {
    return processedCount;
  }

  void resetProcessedCount() {
    processedCount = 0;
    successCount = 0;
    errorCount = 0;
  }

  public int getSuccessCount() {
    return successCount;
  }

  void incrementSuccessCount() {
    successCount++;
    processedCount++;
  }

  public int getErrorCount() {
    return errorCount;
  }

  void incrementErrorCount() {
    errorCount++;
    processedCount++;
  }
}
