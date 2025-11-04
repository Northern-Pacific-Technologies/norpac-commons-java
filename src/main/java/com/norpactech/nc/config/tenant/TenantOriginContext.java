package com.norpactech.nc.config.tenant;

import java.util.UUID;

public class TenantOriginContext {

  private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
  
  private TenantOriginContext() {}

  public static void setIdTenantOrigin(String idTenantOrigin) { 
    CURRENT.set(idTenantOrigin); 
  }
  
  public static String getIdTenantOrigin() { 
    return CURRENT.get(); 
  }
 
  public static UUID getUUID() { 
    return UUID.fromString(CURRENT.get()); 
  }
  
  public static void clear() { 
    CURRENT.remove(); 
  }  
}
