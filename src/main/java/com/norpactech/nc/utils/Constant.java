package com.norpactech.nc.utils;

import java.time.format.DateTimeFormatter;

public final class Constant {
  private Constant() {}
  
  public static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
}
