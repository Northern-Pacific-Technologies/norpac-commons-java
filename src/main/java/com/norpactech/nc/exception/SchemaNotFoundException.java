package com.norpactech.nc.exception;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
public class SchemaNotFoundException extends Exception {

  private static final long serialVersionUID = 6999603998166860929L;

  public SchemaNotFoundException(String tenant, String schema) {
    super("Schema <" + schema + "> was not found for Tenant <" + tenant + ">");
  }
}
