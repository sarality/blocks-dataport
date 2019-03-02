package com.sarality.dataport.file.exporter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable Progress Status for File Export/Import
 *
 * @author satya@ (Satya Puniani)
 */
public class FileExportProgress implements Parcelable {

  private int maxProgress;
  private int progress;

  FileExportProgress(int progress, int maxProgress) {
    this.progress = progress;
    this.maxProgress = maxProgress;
  }

  public int getMaxProgress() {
    return maxProgress;
  }

  public int getProgress() {
    return progress;
  }

  public static final Creator CREATOR = new Creator() {
    @Override
    public FileExportProgress createFromParcel(Parcel source) {
      return new FileExportProgress(
          source.readInt(),
          source.readInt());
    }

    @Override
    public FileExportProgress[] newArray(int size) {
      return new FileExportProgress[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(progress);
    dest.writeInt(maxProgress);
  }
}
