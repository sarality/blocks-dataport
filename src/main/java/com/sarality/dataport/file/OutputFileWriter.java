package com.sarality.dataport.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes a File given a filename and directoryPath
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class OutputFileWriter {

  private static final Logger logger = LoggerFactory.getLogger(OutputFileWriter.class);

  private final String fileName;
  private final String directoryPath;

  private BufferedWriter bufferedWriter = null;

  public OutputFileWriter(FileInfo fileData) {
    this.fileName = fileData.getFileName();
    this.directoryPath = fileData.getDirectoryPath();
  }


  public void open() throws IOException {
    bufferedWriter = new BufferedWriter(new FileWriter(new File(directoryPath, fileName)));
  }

  public void close() throws IOException {
    if (bufferedWriter != null) {
      bufferedWriter.close();
    }
  }

  public void writeToFile(String line) throws IOException {
    logger.debug("Writing line to file {} ", line);
    bufferedWriter.write(line);
    bufferedWriter.newLine();
    bufferedWriter.flush();
  }
}
