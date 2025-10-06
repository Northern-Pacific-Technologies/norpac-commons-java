package com.norpactech.nc.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * For details, see the LICENSE file in this project root.
 */
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static DateTimeFormatter toDayMinuteSeconds = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss Z");  
  /**
   * Formats a ZonedDateTime to a string representation with day, minutes, and seconds.
   * Uses the pattern "MM-dd-yyyy HH:mm:ss Z" for formatting.
   * 
   * @param zonedDateTime the ZonedDateTime to format, can be null
   * @return the formatted date string, or "null" if the input is null
   */
  public static String formatDayMinutesSeconds(ZonedDateTime zonedDateTime) {

    if (zonedDateTime == null) {
      return "null";
    }
    return toDayMinuteSeconds.format(zonedDateTime);
  }
  /**
   * Converts a java.sql.Date to an Instant.
   * 
   * @param source the java.sql.Date to convert, can be null
   * @return the corresponding Instant, or null if the input is null
   */
  public static Instant toInstant(java.sql.Date source) {
    
    if (source == null) {
      return null;
    }
    return Instant.ofEpochMilli(source.getTime());
  }
}