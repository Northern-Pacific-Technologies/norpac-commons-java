package com.norpactech.nc.api.utils;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.norpactech.nc.enums.EnumApiCodes;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Meta {

  private EnumApiCodes apiCode;
  private String errorCode = "";
  private String message = "";
  private String hint = ""; 
  private String detail = "";
  private Long count = null;
  private Timestamp timestamp = new Timestamp(new Date().getTime());

  public Meta() {}
  
  public Meta (EnumApiCodes apiCode) {
    this.apiCode = apiCode;
  }

  public Meta(EnumApiCodes apiCode, String errorCode, String message, String hint, String detail) {
    
    this.apiCode = apiCode;
    this.errorCode = errorCode;
    this.message = message;
    this.hint = hint;
    this.detail = detail;
  
  }
  
  public EnumApiCodes getApiCode() {
    return apiCode;
  }

  public void setApiCode(EnumApiCodes apiCode) {
    this.apiCode = apiCode;
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

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
