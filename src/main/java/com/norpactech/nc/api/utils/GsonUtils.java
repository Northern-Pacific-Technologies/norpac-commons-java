package com.norpactech.nc.api.utils;

import java.sql.Timestamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

  private static final Gson GSON_INSTANCE = new GsonBuilder()
      .registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
      .create();

  public static Gson getGson() {
    return GSON_INSTANCE;
  }

  public static Timestamp parseTimestamp(String timestampString) {
    return GSON_INSTANCE.fromJson("\"" + timestampString + "\"", Timestamp.class);
  }
}