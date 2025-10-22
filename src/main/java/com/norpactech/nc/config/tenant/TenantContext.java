package com.norpactech.nc.config.tenant;

public class TenantContext {

  private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
  
  private TenantContext() {}

  public static void setIdTenant(String idTenant) { 
    CURRENT.set(idTenant); 
  }
  
  public static String getIdTenant() { 
    return CURRENT.get(); 
  }
  
  public static void clear() { 
    CURRENT.remove(); 
  }  
}
