package com.norpactech.nc.config.tenant;

import java.util.UUID;

public class TenantContext {

  private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
  
  private TenantContext() {}

  public static void setIdTenant(String idTenant) { 
    CURRENT.set(idTenant); 
  }
  
  public static String getIdTenant() { 
    return CURRENT.get(); 
  }
 
  public static UUID getUUID() { 
    return UUID.fromString(CURRENT.get()); 
  }
  
  public static void clear() { 
    CURRENT.remove(); 
  }  
}
