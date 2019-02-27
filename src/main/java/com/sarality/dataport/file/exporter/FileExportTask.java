package com.sarality.dataport.file.exporter;

import com.sarality.dataport.file.Delimiter;
import com.sarality.dataport.file.FileInfo;
import com.sarality.dataport.file.OutputFileWriter;
import com.sarality.datasource.DataSource;
import com.sarality.form.FormDataConverter;
import com.sarality.form.FormField;
import com.sarality.task.ProgressCount;
import com.sarality.task.Task;
import com.sarality.task.TaskProgressPublisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Task that exports data to a Delimited File.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FileExportTask<T> implements Task<FileInfo, ProgressCount, FileExportStatus> {
  private final DataSource<List<T>> dataSource;
  private final FileLineGenerator<T> lineGenerator;

  public FileExportTask(DataSource<List<T>> dataSource,
      List<FormField> fieldList, FormDataConverter<T> dataConverter, Delimiter delimiter) {
    this(dataSource, fieldList, dataConverter, new ArrayList<FormDataTransformer>(), delimiter);
  }

  public FileExportTask(DataSource<List<T>> dataSource, List<FormField> fieldList,
      FormDataConverter<T> dataConverter, List<FormDataTransformer> transformers, Delimiter delimiter) {
    this.dataSource = dataSource;
    this.lineGenerator = new FormDataLineGenerator<>(fieldList, dataConverter, transformers, delimiter);
  }

  private FileExportStatus export(FileInfo exportFile, TaskProgressPublisher<ProgressCount> progressPublisher) {
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
    updateProgress(progressPublisher, new ProgressCount(numItems, 0));
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
      updateProgress(progressPublisher, new ProgressCount(numItems, ctr));
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

  private void updateProgress(TaskProgressPublisher<ProgressCount> progressPublisher, ProgressCount progress) {
    if (progressPublisher != null) {
      progressPublisher.updateProgress(progress);
    }
  }

  @Override
  public FileExportStatus execute(FileInfo exportFile, TaskProgressPublisher<ProgressCount> progressPublisher) {
    return export(exportFile, progressPublisher);
  }
}
