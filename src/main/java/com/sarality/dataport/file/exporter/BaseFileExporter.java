package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.Delimiter;
import com.sarality.dataport.file.FileInfo;
import com.sarality.dataport.file.OutputFileWriter;
import com.sarality.datasource.DataSource;
import com.sarality.db.Table;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;
import com.sarality.task.TaskProgressPublisher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hirondelle.date4j.DateTime;

/**
 * File exporter class that loads, transforms and exports data to a specified file
 *
 * @author satya (satya puniani)
 */
public class BaseFileExporter<T> {

  private final String BASE_FILE_NAME = "EXPORT.tsv";

  private final Table<T> table;
  private final DataSource<List<T>> dataSource;
  private final FileLineGenerator<T> lineGenerator;

  public BaseFileExporter(Table<T> table, DataSource<List<T>> dataSource, List<FormField> fieldList,
      FormDataConverter<T> dataConverter, List<FormDataTransformer> transformers, Delimiter delimiter) {
    this.table = table;
    this.dataSource = dataSource;
    this.lineGenerator = new FormDataLineGenerator<>(fieldList, dataConverter, transformers, delimiter);
  }

  public String getFileName(DateTime exportDate) {
    return getFileName(BASE_FILE_NAME, exportDate);
  }

  public FileExportStatus export(FileInfo exportFile, TaskProgressPublisher<FileExportProgress> progressPublisher) {
    OutputFileWriter writer = new OutputFileWriter(exportFile);
    try {
      writer.open();
    } catch (IOException ioe) {
      return new FileExportStatus(exportFile.getFileName(), exportFile.getDirectoryPath(), false, ioe.getMessage());
    }
    List<T> dataList;
    try {
      dataSource.load();
      dataList = dataSource.getData();
    } catch (Throwable t) {
      return new FileExportStatus(exportFile.getFileName(), exportFile.getDirectoryPath(), false, t.getMessage());
    }

    // initialize the transformers
    lineGenerator.init();
    // Add Headers
    try {
      writer.writeToFile(lineGenerator.generateHeader());
    } catch (Throwable t) {
      return new FileExportStatus(exportFile.getFileName(), exportFile.getDirectoryPath(), false, t.getMessage());
    }

    int numItems = dataList.size();
    updateProgress(progressPublisher, new FileExportProgress(numItems, 0));
    int ctr = 0;
    int numSuccesses = 0;
    int numFailures = 0;

    for (T data : dataList) {
      String line = lineGenerator.generateLine(data);
      try {
        writer.writeToFile(line);
        numSuccesses++;
      } catch (IOException ioe) {
        numFailures++;
      }
      ctr++;
      updateProgress(progressPublisher, new FileExportProgress(numItems, ctr));
    }
    try {
      writer.close();
    } catch (IOException e) {
      return new FileExportStatus(exportFile.getFileName(), exportFile.getDirectoryPath(), false, e.getMessage(),
          numItems, numSuccesses, numFailures);
    }
    return new FileExportStatus(exportFile.getFileName(), exportFile.getDirectoryPath(), true, null,
        numItems, numSuccesses, numFailures);
  }

  private void updateProgress(TaskProgressPublisher<FileExportProgress> progressPublisher,
      FileExportProgress progress) {
    if (progressPublisher != null) {
      progressPublisher.updateProgress(progress);
    }
  }

  private String getFileName(String fileName, DateTime exportDate) {
    String extension = "";

    int extensionIndex = fileName.lastIndexOf('.');
    if (extensionIndex > 0) {
      extension = fileName.substring(extensionIndex);
      fileName = fileName.substring(0, extensionIndex);
    }
    return fileName
        .concat("_")
        .concat(table.getName())
        .concat(exportDate.format("_MMM_DD", Locale.getDefault()))
        .concat(extension);
  }

}
