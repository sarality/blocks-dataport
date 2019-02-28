package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.FileInfo;
import com.sarality.task.TaskProgressPublisher;

import hirondelle.date4j.DateTime;

/**
 * Interface for file export
 *
 * @author satya (satya puniani)
 */
public interface FileExporter<T> {

  /**
   * Provide a file name. Date is provided so it can be appended
   *
   * @param exportDate DateTime on which the export is being generated
   * @return name to be used for the file
   */
  String getFileName(DateTime exportDate);

  /**
   * Exports data to a file
   *
   * @param exportFile File to export to
   * @param progressPublisher update progress back to app
   * @return status of the export - success or failure, and the path to the exported file
   */
  FileExportStatus export(FileInfo exportFile, TaskProgressPublisher<FileExportProgress> progressPublisher);

}
