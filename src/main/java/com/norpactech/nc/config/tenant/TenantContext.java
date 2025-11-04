package com.norpactech.nc.config.tenant;

import java.util.UUID;

public class TenantContext {

  private static UUID idTenant;
  
  public static void setIdTenant(UUID idTenant) { 
    TenantContext.idTenant = idTenant;
  }
  
  public static UUID getIdTenant() { 
    return idTenant; 
  }
}
