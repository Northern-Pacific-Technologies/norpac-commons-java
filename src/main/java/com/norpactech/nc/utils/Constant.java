package com.norpactech.nc.utils;

import java.time.format.DateTimeFormatter;

public final class Constant {
  private Constant() {}
  
  public static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  
  public static final String TRUE = "TRUE";
  public static final String FALSE = "FALSE";
  
  public static final String LIMIT = "limit";
  public static final String PAGE = "page";
  public static final String OFFSET = "offset";
  public static final String SORT_COLUMN = "sortColumn";
  public static final String SORT_DIRECTION = "sortDirection";
  
}
