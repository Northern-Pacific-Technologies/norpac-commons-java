package com.norpactech.nc.enums;

public enum EnumStatus {
  
  OK("OK"), 
  ERROR("ERROR"); 

  private String name;

  public static EnumStatus fromName(String name) {
    for (EnumStatus context : EnumStatus.values()) {
      if (context.getName().equals(name)) {
        return context;
      }
    }
    return null;
  }
  
  private EnumStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
