package com.norpactech.nc.api.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class TimestampTypeAdapter extends TypeAdapter<Timestamp> {

  private static final String[] DATE_FORMATS = {
      "MMM dd, yyyy, h:mm:ss a",    // "Jan 01, 2025, 12:30:45 PM"
      "yyyy-MM-dd'T'HH:mm:ss",      // "2025-01-01T12:30:45"
      "yyyy-MM-dd HH:mm:ss",        // "2025-01-01 12:30:45"
      "yyyy-MM-dd'T'HH:mm:ss.SSS",  // "2025-01-01T12:30:45.123"
  };

  @Override
  public void write(JsonWriter out, Timestamp value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.toInstant().toString());
    }
  }

  @Override
  public Timestamp read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    String dateString = in.nextString();
    return parseTimestamp(dateString);
  }

  private Timestamp parseTimestamp(String dateString) throws IOException {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    // Handle ISO 8601 format with Z timezone and fractional seconds
    if (dateString.endsWith("Z")) {
      try {
        Instant instant = Instant.parse(dateString);
        Instant truncatedInstant = instant.truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
        Timestamp timestamp = Timestamp.from(truncatedInstant);
        java.util.TimeZone localTimeZone = java.util.TimeZone.getDefault();
        int offsetInMillis = localTimeZone.getOffset(timestamp.getTime());
        timestamp = new Timestamp(timestamp.getTime() - offsetInMillis);
        timestamp.setNanos(0);
        return timestamp;
      } catch (DateTimeParseException e) {
        // Fall through to other formats
      }
    }

    for (String format : DATE_FORMATS) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        java.util.Date date = sdf.parse(dateString);
        Timestamp timestamp = new Timestamp(date.getTime());
        timestamp.setNanos(0);
        return timestamp;
      } catch (ParseException e) {
        // Try next format
      }
    }

    // If all formats fail, throw an exception
    throw new IOException("Unable to parse timestamp: " + dateString);
  }
}