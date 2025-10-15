package com.norpactech.nc.exception;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
public class ObjectNotFoundException extends Exception {

 private static final long serialVersionUID = -1861682772106179648L;

  public ObjectNotFoundException(String tenant, String schema, String objectName) {
    super("Object was not found for Tenant <" + tenant + "> and Schema <" + schema + "> and Object <" + objectName + ">.");
  }
}
