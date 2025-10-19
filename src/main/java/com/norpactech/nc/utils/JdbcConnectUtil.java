package com.norpactech.nc.utils;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcConnectUtil {

  private static final Logger logger = LoggerFactory.getLogger(JdbcConnectUtil.class);
  
  public static Connection connection = null;

  public static void connect(String url, String clazz, String username, String password) throws Exception {
    
    try {
      Class.forName(clazz);
    } 
    catch (ClassNotFoundException e) {
      throw new Exception("Failed to load DB driver class: " + clazz, e);
    }

    try {
      connection = DriverManager.getConnection(url, username, password);
    }
    catch (Exception e) {
      throw new Exception("Failed to connect to the source database " + e.getMessage());      
    }
    logger.info("Source Database Connected");
  }  
  
  public static void disconnect() {
    
    try {
      connection.close();
    }
    catch (Exception e) { }
    logger.info("Source Database Disconnected");
  }    
  
}
