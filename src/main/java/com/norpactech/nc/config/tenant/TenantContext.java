package com.norpactech.nc.config.tenant;

import java.util.UUID;

public final class TenantContext {

  private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();

  private TenantContext() {}

  public static void setId(UUID idTenant) {
    TENANT_ID.set(idTenant.toString());
  }

  public static void setId(String idTenant) {
    TENANT_ID.set(idTenant);
  }

  public static String getId() {
    return TENANT_ID.get();
  }

  public static UUID getUUID() {
    return UUID.fromString(UUID.fromString(TENANT_ID.get()).toString());
  }

  public static void clear() {
    TENANT_ID.remove();
  }
}