package com.norpactech.nc.model;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norpactech.nc.api.utils.ApiResponse;
import com.norpactech.nc.api.utils.GenericSelectResponse;
import com.norpactech.nc.utils.Constant;
import com.norpactech.nc.utils.TextUtils;

public class BaseModel {

  @JsonIgnore
  private Map<String, Object> properties = new HashMap<>();

  public BaseModel() {}

  public BaseModel(Object data) {
    map(data);
  }
  
  public BaseModel(ApiResponse response) {

    GenericSelectResponse data = (GenericSelectResponse) response.getData();
    map(data);
  }
  
  public BaseModel(GenericSelectResponse data) {
    
    map(data);
  }
  
  protected void map(GenericSelectResponse data) {
    
    for (Map.Entry<String, Object> entry : data.getProperties().entrySet()) {
      try {
        String property = TextUtils.toCamelCase(entry.getKey());
        Field field = this.getClass().getDeclaredField(property);
        field.setAccessible(true);
        field.set(this, entry.getValue());
      } 
      catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException("Error mapping property '" + entry.getKey() + "': " + e.getMessage(), e);
      }
    } 
  }

  protected void map(Object data) {
    
    if (data == null) {
      return;
    }

    for (Field field : this.getClass().getDeclaredFields()) {
      try {
        field.setAccessible(true); // Make the field accessible
        String propertyName = field.getName();

        Field dataField = data.getClass().getDeclaredField(propertyName);
        dataField.setAccessible(true);
        Object value = dataField.get(data);
        field.set(this, value);
        
      } 
      catch (NoSuchFieldException e) {
        // Ignore
      } 
      catch (IllegalAccessException e) {
        throw new RuntimeException("Error mapping property '" + field.getName() + "': " + e.getMessage(), e);
      }
    }
  }
  
  /**
   * Query parameters are always string values brought in from the API
   * 
   * @param queryParams
   * @return map with name/value converted into Objects
   * @throws Exception
   */
  protected static Map<String, Object> paramMatcher(Map<String, String> queryParams, Class<?> thisClazz) throws Exception {
    
    var request = new LinkedHashMap<String, Object>();

    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      try {
        
        String property = TextUtils.toCamelCase(key);
        Field field = thisClazz.getDeclaredField(property);
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        Object convertedValue;

        if (fieldType == UUID.class) {
          convertedValue = UUID.fromString(value);
        } 
        else if (fieldType == Integer.class || fieldType == int.class) {
          convertedValue = Integer.parseInt(value);
        } 
        else if (fieldType == Float.class || fieldType == float.class) {
          convertedValue = Double.parseDouble(value);
        } 
        else if (fieldType == Double.class || fieldType == double.class) {
          convertedValue = Double.parseDouble(value);
        } 
        else if (fieldType == Boolean.class || fieldType == boolean.class) {
          convertedValue = Boolean.parseBoolean(value);
        } 
        else if (fieldType == java.util.Date.class) {
          convertedValue = parseDateOrTimestamp(value, fieldType);
        } 
        else if (fieldType == java.sql.Timestamp.class) {
          convertedValue = parseDateOrTimestamp(value, fieldType);
        } 
        else if (fieldType == java.time.LocalDate.class) {
          convertedValue = java.time.LocalDate.parse(value);
        } 
        else if (fieldType == java.time.LocalDateTime.class) {
          convertedValue = java.time.LocalDateTime.parse(value);
        }        
        else {
          convertedValue = value; // Default to String or other types
        }
        request.put(property, convertedValue);
      } 
      catch (Exception e) {
        if (key.equalsIgnoreCase(Constant.LIMIT)
        ||  key.equalsIgnoreCase(Constant.OFFSET)
        ||  key.equalsIgnoreCase(Constant.SORT_COLUMN)
        ||  key.equalsIgnoreCase(Constant.SORT_DIRECTION)) {
          request.put(key, value);
        }
        else {
          throw new Exception("Error converting query parameter '" + key + "' with value '" + value + "': " + e.getMessage(), e);
        }
      }
    }
    return request;
  }
  
  private static Object parseDateOrTimestamp(String value, Class<?> fieldType) {
    
    String[] dateFormats = {
        "yyyy-MM-dd",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "MM/dd/yyyy",
        "MM/dd/yyyy HH:mm:ss"
    };

    for (String format : dateFormats) {
      try {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (fieldType == java.util.Date.class) {
          return dateFormat.parse(value);
        } 
        else if (fieldType == java.sql.Timestamp.class) {
          java.util.Date parsedDate = dateFormat.parse(value);
          return new java.sql.Timestamp(parsedDate.getTime());
        }
      } 
      catch (ParseException ignored) {
        // Try the next format
      }
    }
    return null;
  }
  
  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }
}