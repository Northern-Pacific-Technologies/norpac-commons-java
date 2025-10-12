package com.norpactech.nc.config.json;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.norpactech.nc.utils.Constant;

public class GsonConfig {

  /**
   * Provides a preconfigured GsonBuilder with NorPac defaults, allowing subclasses
   * to customize before creating the Gson instance.
   */
  protected GsonBuilder baseBuilder() {
    return new GsonBuilder()
        .registerTypeAdapter(Character.class, new JsonSerializer<Character>() {
          @Override
          public JsonElement serialize(Character src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
          }
        })
        .registerTypeAdapter(Character.class, new JsonDeserializer<Character>() {
          @Override
          public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String str = json.getAsString();
            if (str == null || str.isEmpty()) {
              return null;
            }
            return str.charAt(0);
          }
        })  
        .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
          @Override
          public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Constant.TIMESTAMP_FORMATTER.format(src));
          }
        })
        .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
          @Override
          public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), Constant.TIMESTAMP_FORMATTER);
          }
        })
        .registerTypeAdapter(java.sql.Timestamp.class, new JsonSerializer<java.sql.Timestamp>() {
          @Override
          public JsonElement serialize(java.sql.Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Constant.TIMESTAMP_FORMATTER.format(src.toLocalDateTime()));
          }
        })
        .registerTypeAdapter(java.sql.Timestamp.class, new JsonDeserializer<java.sql.Timestamp>() {
          @Override
          public java.sql.Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return java.sql.Timestamp.valueOf(LocalDateTime.parse(json.getAsString(), Constant.TIMESTAMP_FORMATTER));
          }
        })
        .registerTypeAdapter(java.sql.Date.class, new JsonSerializer<java.sql.Date>() {
          @Override
          public JsonElement serialize(java.sql.Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Constant.TIMESTAMP_FORMATTER.format(src.toLocalDate().atStartOfDay()));
          }
        })
        .registerTypeAdapter(java.sql.Date.class, new JsonDeserializer<java.sql.Date>() {
          @Override
          public java.sql.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return java.sql.Date.valueOf(LocalDateTime.parse(json.getAsString(), Constant.TIMESTAMP_FORMATTER).toLocalDate());
          }
        })
        .registerTypeAdapter(java.util.Date.class, new JsonSerializer<java.util.Date>() {
          @Override
          public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Constant.TIMESTAMP_FORMATTER.format(src.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()));
          }
        })
        .registerTypeAdapter(java.util.Date.class, new JsonDeserializer<java.util.Date>() {
          @Override
          public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return java.util.Date.from(LocalDateTime.parse(json.getAsString(), Constant.TIMESTAMP_FORMATTER).atZone(java.time.ZoneId.systemDefault()).toInstant());
          }
        })          
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  }

  public Gson gson() {
    return baseBuilder().create();
  }
}