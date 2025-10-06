package com.norpactech.nc.vo;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * For details, see the LICENSE file in this project root.
 */
import lombok.Data;
/**
 * Value Object representing a downloadable file with its path and content.
 * This class is used to encapsulate file information for download operations.
 */
@Data
public class DownloadFileVO {

  private String filePath;
  private String fileContext;
  /**
   * Constructs a new DownloadFileVO with the specified file path and content.
   * 
   * @param filePath the path or name of the file
   * @param fileContext the content of the file as a string
   */
  public DownloadFileVO(String filePath, String fileContext) {
    
    this.filePath = filePath;
    this.fileContext = fileContext;
  }
}