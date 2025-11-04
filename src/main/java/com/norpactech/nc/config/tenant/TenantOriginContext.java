package com.norpactech.nc.config.tenant;

import java.util.UUID;

public final class TenantOriginContext {

  private static final ThreadLocal<UUID> TENANT_ORIGIN_ID = new ThreadLocal<>();

  private TenantOriginContext() {}

  public static void setIdTenantOrigin(UUID idTenantOrigin) {
    TENANT_ORIGIN_ID.set(idTenantOrigin);
  }

  public static UUID getIdTenantOrigin() {
    return TENANT_ORIGIN_ID.get();
  }

  public static void clear() {
    TENANT_ORIGIN_ID.remove();
  }
}