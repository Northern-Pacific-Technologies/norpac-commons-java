package com.norpactech.nc.exception;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * This software is proprietary and may not be used, copied, modified, or distributed 
 * without permission. It is provided "as is" without warranties of any kind. 
 * 
 * For details, see the LICENSE file in this project root.
 */
import java.util.UUID;

public class RefTableNotFoundException extends Exception {

  private static final long serialVersionUID = 8741513207026307347L;

  public RefTableNotFoundException(UUID id) {
    super("Reference Table Entry for UUID <" + id + "> not found.");
  }
}
