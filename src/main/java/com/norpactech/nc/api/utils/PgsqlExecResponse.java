package com.norpactech.nc.api.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.norpactech.nc.api.exception.ApiResponseException;
import com.norpactech.nc.enums.EnumStatus;

public class PgsqlExecResponse {

  private static final Logger logger = Logger.getLogger(PgsqlExecResponse.class.getName());
  private String status;
  private Object data;     
  private Object errors;   
  private String errorCode;
  private String message;
  private String hint; 
  private String detail;
  
  public PgsqlExecResponse() {}   
  
  public PgsqlExecResponse(ResultSet rs) {   
    
    String input = null;
    try {
      input = rs.getObject(1).toString();
      logger.fine("Raw PostgreSQL response: " + input);
    }
    catch (SQLException e) {
      throw new ApiResponseException("Failed to extract the Global Database Response: " + e.getMessage());
    }
    List<String> parsed = ParseUtils.parse(input);
    if (parsed.size() < 1) {
      throw new ApiResponseException("Invalid PostgreSQL response format - insufficient fields");
    }
    this.status = ParseUtils.stripQuotes(parsed.get(0));

    if (EnumStatus.OK.getName().equalsIgnoreCase(this.status)) {
      this.data = parsed.size() > 1 ? ParseUtils.parseJson(parsed.get(1)) : null;
      this.errors = null;
    } 
    else {
      this.data = null;
      
      // Parse all error-related fields first
      this.errorCode = parsed.size() > 3 ? ParseUtils.stripQuotes(parsed.get(3)) : "";
      this.message = parsed.size() > 4 ? ParseUtils.stripQuotes(parsed.get(4)) : "";
      this.hint = parsed.size() > 5 ? ParseUtils.stripQuotes(parsed.get(5)) : "";
      this.detail = parsed.size() > 6 ? ParseUtils.stripQuotes(parsed.get(6)) : "";
      
      // Try to parse JSON errors first, fallback to creating error from other fields
      String errorsStr = parsed.size() > 2 ? parsed.get(2) : "";
      this.errors = parseErrors(errorsStr);
    }
    
    // For success cases, still parse the optional fields
    if (EnumStatus.OK.getName().equalsIgnoreCase(this.status)) {
      this.errorCode = parsed.size() > 3 ? ParseUtils.stripQuotes(parsed.get(3)) : "";
      this.message = parsed.size() > 4 ? ParseUtils.stripQuotes(parsed.get(4)) : "";
      this.hint = parsed.size() > 5 ? ParseUtils.stripQuotes(parsed.get(5)) : "";
      this.detail = parsed.size() > 6 ? ParseUtils.stripQuotes(parsed.get(6)) : "";
    }
  }
  
  private Object parseErrors(String errorsStr) {
    String cleanedErrors = ParseUtils.stripQuotes(errorsStr);
    
    // If we have actual JSON error content, parse it
    if (cleanedErrors != null && !cleanedErrors.trim().isEmpty()) {
      logger.fine("Parsing JSON errors: " + cleanedErrors);
      
      try {
        cleanedErrors = cleanedErrors.replace("\"\"", "\"");

        if ((cleanedErrors.startsWith("\"[") && cleanedErrors.endsWith("]\"")) ||
            (cleanedErrors.startsWith("\"{") && cleanedErrors.endsWith("}\""))) {
          cleanedErrors = cleanedErrors.substring(1, cleanedErrors.length() - 1);
        }
        
        JsonElement element = JsonParser.parseString(cleanedErrors);
        
        if (element.isJsonArray()) {
          return element.getAsJsonArray();
        } 
        else if (element.isJsonObject()) {
          JsonArray array = new JsonArray();
          array.add(element);
          return array;
        } 
        else {
          JsonArray array = new JsonArray();
          array.add(element);
          return array;
        }        
      } 
      catch (Exception e) {
        logger.warning("Failed to parse error JSON: " + e.getMessage() + ". Raw: " + errorsStr);
        // Fall through to create error from other fields
      }
    }    
    // No JSON errors provided, but we have an error status
    // Create a JSON error object from the available error information
    if (!EnumStatus.OK.getName().equalsIgnoreCase(this.status)) {
      JsonArray errorArray = new JsonArray();
      
      // Determine error type based on error code
      String errorType = "database";
      if ("23514".equals(this.errorCode)) {
        errorType = "validation";
      } else if ("23502".equals(this.errorCode) || "23503".equals(this.errorCode) || "23505".equals(this.errorCode)) {
        errorType = "constraint";
      }
      
      // Create error object with available information and fix double quotes
      StringBuilder errorMessage = new StringBuilder();
      if (this.message != null && !this.message.isEmpty()) {
        // Fix double quotes in the message
        String cleanMessage = this.message.replace("\"\"", "\"");
        errorMessage.append(cleanMessage);
      }
      if (this.detail != null && !this.detail.isEmpty()) {
        if (errorMessage.length() > 0) {
          errorMessage.append(": ");
        }
        // Fix double quotes in the detail
        String cleanDetail = this.detail.replace("\"\"", "\"");
        errorMessage.append(cleanDetail);
      }
      
      // Create the error JSON object with proper quote escaping
      String finalMessage = errorMessage.toString().replace("\"", "\\\"");
      
      JsonElement errorObj = JsonParser.parseString(String.format(
        "{\"type\": \"%s\", \"code\": \"%s\", \"message\": \"%s\"}",
        errorType,
        this.errorCode != null ? this.errorCode : "",
        finalMessage
      ));
      
      errorArray.add(errorObj);
      logger.fine("Created error object from database response: " + errorArray.toString());
      return errorArray;
    }    
    // Default: return empty array
    return new JsonArray();
  }
  
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public Object getData() {
    return data;
  }
  public void setData(Object data) {
    this.data = data;
  }
  public Object getErrors() {
    return errors;
  }
  public void setErrors(Object errors) {
    this.errors = errors;
  }
  public String getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getHint() {
    return hint;
  }
  public void setHint(String hint) {
    this.hint = hint;
  }
  public String getDetail() {
    return detail;
  }
  public void setDetail(String detail) {
    this.detail = detail;
  }
}