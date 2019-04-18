package com.sarality.dataport.file.importer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Progress from the Import Task
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileImportProgress implements Parcelable {

  private final String taskName;
  private final int totalItemCount;
  private int processedCount;
  private int successCount;
  private int errorCount;
  private int skippedCount;

  public FileImportProgress(String taskName, int totalItemCount, int processedCount,
      int successCount, int errorCount, int skippedCount) {
    this.taskName = taskName;
    this.totalItemCount = totalItemCount;
    this.processedCount = processedCount;
    this.successCount = successCount;
    this.errorCount = errorCount;
    this.skippedCount = skippedCount;
  }

  FileImportProgress(int totalItemCount, int processedCount, int successCount, int errorCount,
      int skippedCount) {
    this("", totalItemCount, processedCount, successCount, errorCount, skippedCount);
  }

  public String getTaskName() {
    return taskName;
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

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    @Override
    public FileImportProgress createFromParcel(Parcel source) {
      return new FileImportProgress(
          source.readString(),
          source.readInt(),
          source.readInt(),
          source.readInt(),
          source.readInt(),
          source.readInt());
    }

    @Override
    public FileImportProgress[] newArray(int size) {
      return new FileImportProgress[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(taskName);
    dest.writeInt(totalItemCount);
    dest.writeInt(processedCount);
    dest.writeInt(successCount);
    dest.writeInt(errorCount);
    dest.writeInt(skippedCount);
  }

}
