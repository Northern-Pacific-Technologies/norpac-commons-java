package com.norpactech.nc.config.json;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  
  @Override
  public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(formatter.format(date));
  }

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    try {
      return formatter.parse(json.getAsString());
    } catch (Exception e) {
      throw new JsonParseException(e);
    }
  }
}