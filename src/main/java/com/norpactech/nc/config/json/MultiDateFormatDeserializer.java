package com.norpactech.nc.config.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class MultiDateFormatDeserializer extends JsonDeserializer<Timestamp> {

  private static final List<String> DATE_FORMATS = Arrays.asList(
      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", // ISO-8601 format with Z timezone
      "yyyy-MM-dd'T'HH:mm:ss'Z'",      // ISO-8601 format with Z timezone, no milliseconds
      "MMM dd, yyyy, h:mm:ss a",
      "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      "yyyy-MM-dd HH:mm:ss",
      "MM/dd/yyyy HH:mm:ss",
      "MMM dd, yyyy, h:mm:ss a"
      );

  @Override
  public Timestamp deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    String dateStr = parser.getText();
    
    if (dateStr == null || dateStr.isEmpty()) {
      return null;
    }
    // Normalize Unicode spaces to regular space (handles U+202F, U+00A0, etc.)
    dateStr = dateStr.replaceAll("\\s", " ").replace('\u202F', ' ').replace('\u00A0', ' ');
    // Remove duplicate spaces
    dateStr = dateStr.replaceAll(" +", " ").trim();
    
    // First try to parse using Java 8 Instant for ISO-8601 format with Z
    try {
      Instant instant = Instant.parse(dateStr);
      return Timestamp.from(instant);
    } catch (DateTimeParseException e) {
      // If that fails, try the SimpleDateFormat patterns
    }
    
    // Then try the SimpleDateFormat patterns
    for (String format : DATE_FORMATS) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Strict parsing to avoid incorrect dates
        return new Timestamp(sdf.parse(dateStr).getTime());
      } catch (ParseException e) {
        // Try the next format
      }
    }
    
    // If we get here, none of the patterns matched
    throw new IOException("Unable to parse date: " + dateStr + ". Supported formats include: " 
        + String.join(", ", DATE_FORMATS) + " and ISO-8601 with Z timezone.");
  }
}