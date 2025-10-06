package com.norpactech.nc.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * For details, see the LICENSE file in this project root.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.norpactech.nc.vo.DownloadFileVO;

public class FileUtils {

  /**
   * Extracts files from a zipped byte array and saves them to the file system.
   * Files are extracted to the specified download path.
   * 
   * @param downloadPath the directory path where extracted files should be saved
   * @param zipped the byte array containing the zipped data
   * @return an empty string (StringBuilder is not populated in current implementation)
   * @throws IOException if an I/O error occurs during zip extraction or file writing
   */
  public static String zipToFiles(String downloadPath, byte[] zipped) throws IOException {
    
    var sb = new StringBuilder();
    try (
      var byteArrayInputStream = new ByteArrayInputStream(zipped);
      var zipInputStream = new ZipInputStream(byteArrayInputStream);)  
    {
      var buffer = new byte[1024 * 8];
      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        String fileName = entry.getName();
        
        String filePath = downloadPath + "/" + fileName;
        try (var fileOutputStream = new FileOutputStream(filePath)) {
          int read;
          while ((read = zipInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, read);
          }
        }
      }
    }
    return sb.toString();
  }  
  /**
   * Extracts the contents of a zipped byte array and returns them as a string.
   * Each file's name is included on a separate line followed by its content.
   * 
   * @param zipped the byte array containing the zipped data
   * @return a string containing all file names and contents from the zip
   * @throws IOException if an I/O error occurs during zip extraction
   */
  public static String zipToString(byte[] zipped) throws IOException {
    
    var sb = new StringBuilder();
    try (
      var byteArrayInputStream = new ByteArrayInputStream(zipped);
      var zipInputStream = new ZipInputStream(byteArrayInputStream);)  
    {
      var buffer = new byte[1024 * 8];

      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        sb.append(entry.getName()).append("\n");
        int read;
        while ((read = zipInputStream.read(buffer)) != -1) {
          sb.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
        }
      }
    }
    return sb.toString();
  }  
  /**
   * Extracts a zipped byte array and writes it to a new zip file at the specified path.
   * This method essentially copies/rewrites the zip content to a new file location.
   * 
   * @param filePath the target file path where the zip file should be written
   * @param zipped the byte array containing the zipped data to be written
   * @throws IOException if an I/O error occurs during zip processing or file writing
   */
  public static void zipToFile(String filePath, byte[] zipped) throws IOException {

    try (
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath));
        InputStream byteArrayInputStream = new ByteArrayInputStream(zipped);
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);)
    { 
      byte[] buffer = new byte[1024 * 8];

      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        out.putNextEntry(entry);

        int read;
        while ((read = zipInputStream.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
      }
    }
  }  
  /**
   * Creates a zip file from a list of DownloadFileVO objects and returns it as a byte array.
   * Each file in the list is added as a separate entry in the zip using UTF-8 encoding.
   * 
   * @param files the list of DownloadFileVO objects to be zipped
   * @return a byte array containing the zipped files
   * @throws IOException if an I/O error occurs during zip creation
   */
  public static byte[] filesToZip(List<DownloadFileVO> files) throws IOException {

    var byteArrayOutputStream = new ByteArrayOutputStream();

    try (
      var zipOutputStream = new ZipOutputStream(byteArrayOutputStream);)
    {
      for (var file : files) {
        var zipEntry = new ZipEntry(file.getFilePath());
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(file.getFileContext().getBytes(StandardCharsets.UTF_8));
        zipOutputStream.closeEntry();
      }
    } 
    return byteArrayOutputStream.toByteArray();    
  }  
}