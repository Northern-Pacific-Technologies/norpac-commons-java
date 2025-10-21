package com.norpactech.nc.vo;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
public class JwtClientCredentialsRequestVO {

  private String clientSecret;
  private String scope;
  
  public JwtClientCredentialsRequestVO(
      String clientSecret, 
      String scope) {
    
    this.clientSecret = clientSecret;
    this.scope = scope;
  }

  public String getClientSecret() {
    return clientSecret;
  }
  
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
  
  public String getScope() {
    return scope;
  }
  
  public void setScope(String scope) {
    this.scope = scope;
  }
}
