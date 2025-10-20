package com.norpactech.nc.config.load;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.norpactech.nc.api.utils.ApiResponse;
import com.norpactech.nc.enums.EnumStatus;
import com.norpactech.nc.utils.AuthUtils;
import com.norpactech.nc.utils.NetUtils;
import com.norpactech.nc.vo.JwtClientCredentialsRequestVO;
import com.norpactech.nc.vo.JwtUsernamePasswordRequestVO;

public class ConfiguredAPI {

  private static final Logger logger = LoggerFactory.getLogger(ConfiguredAPI.class);

  public static String host;
  public static String apiVersion;
  public static String jwt;
  public static String dbSchema;

  public static void configure(
      String thatHost, 
      String thatApiVersion,      
      String apiSecret) throws Exception {

    logger.info("Configuring API for Client Credentials Auth...");
    
    if (thatHost != null) {
      host = thatHost;
    }
    else {
      throw new Exception ("Null Host!");
    } 
    
    if (thatApiVersion != null) {
      apiVersion = thatApiVersion;
    }
    else {
      throw new Exception ("Null API Version!");
    }
    
    ApiResponse health = NetUtils.health();
    
    JsonObject jsonObject = JsonParser.parseString(health.getData().toString()).getAsJsonObject();
    String status = jsonObject.get("status").getAsString();
    if (!status.equals(EnumStatus.OK.getName())) {
      throw new Exception("Unhealthy Server. Status: " + status); 
    }
    logger.info("API Health Status: {}", status);
    
    String clientId = "4sknuarei0bakajuguv1us2mop";
    
    JwtClientCredentialsRequestVO jwtRequest = new JwtClientCredentialsRequestVO(clientId, apiSecret);
    jwt = AuthUtils.getJwt(host + "/access-token", jwtRequest);
    logger.info("Service has Signed In");
    
    logger.info("API Successfully Configured");
  }  
  
  public static void configure(
      String thatHost, 
      String thatApiVersion,      
      String username, 
      String password) throws Exception {

    logger.info("Configuring API for Username/Password Auth...");
    
    if (thatHost != null) {
      host = thatHost;
    }
    else {
      throw new Exception ("Null Host!");
    } 
    
    if (thatApiVersion != null) {
      apiVersion = thatApiVersion;
    }
    else {
      throw new Exception ("Null API Version!");
    }
    
    ApiResponse health = NetUtils.health();
    
    JsonObject jsonObject = JsonParser.parseString(health.getData().toString()).getAsJsonObject();
    String status = jsonObject.get("status").getAsString();
    if (!status.equals(EnumStatus.OK.getName())) {
      throw new Exception("Unhealthy Server. Status: " + status); 
    }
    logger.info("United Bins API Health Status: {}", status);
    
    JwtUsernamePasswordRequestVO jwtRequest = new JwtUsernamePasswordRequestVO(username, password);
    jwt = AuthUtils.getJwt(host + "/access-token", jwtRequest);
    logger.info("User '{}' Signed In", jwtRequest.getEmail());
    
    logger.info("United Bins API Successfully Configured");
  }  
}