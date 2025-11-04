package com.norpactech.nc.config.tenant;

import java.util.UUID;

public class TenantOriginContext {

  private static UUID idTenantOrigin;
  
  public static void setIdTenantOrigin(UUID idTenantOrigin) { 
    TenantOriginContext.idTenantOrigin = idTenantOrigin;
  }
  public static UUID getIdTenantOrigin() { 
    return idTenantOrigin; 
  }
}
