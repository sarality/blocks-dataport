package com.sarality.dataport.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Reads a File with  given a filename and directoryPath
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class InputFileReader {

  private static final Logger logger = LoggerFactory.getLogger(InputFileReader.class);

  private final String fileName;
  private final String directoryPath;

  private BufferedReader bufferedReader = null;

  public InputFileReader(FileInfo fileData) {
    this.fileName = fileData.getFileName();
    this.directoryPath = fileData.getDirectoryPath();
  }

  public void open() throws IOException {
    bufferedReader = new BufferedReader(new InputStreamReader(
        new FileInputStream(new File(directoryPath, fileName)), "UTF8"));
  }

  public void close() throws IOException {
    if (bufferedReader != null) {
      bufferedReader.close();
    }
  }

  public void reset() throws IOException {
    bufferedReader.reset();
  }

  public String readLine() throws IOException {
    return bufferedReader.readLine();
  }
}
