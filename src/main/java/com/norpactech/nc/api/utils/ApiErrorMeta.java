package com.norpactech.nc.api.utils;

import com.norpactech.nc.api.enums.EnumApiCodes;

public class ApiErrorMeta extends Meta {
  
  public ApiErrorMeta(EnumApiCodes apiCode, String errorCode, String message, String hint, String detail) {
    super(apiCode, errorCode, message, hint, detail);
  }
}

