package com.norpactech.nc.config.load;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Global environment variables configuration for the Loader
 * This class centralizes all environment variable access and provides
 * validation and default values where appropriate.
 * Automatically loads .env file if present.
 */
public class Globals {

  private static final Logger logger = LoggerFactory.getLogger(Globals.class);
  private static final Dotenv dotenv;

  // API Configuration
  public static final String API_SECRET;
  public static final String API_USERNAME;
  public static final String API_PASSWORD;
  public static final String API_URL;
  public static final String API_VERSION;

  // Database Configuration
  public static final String DB_USERNAME;
  public static final String DB_PASSWORD;
  public static final String DB_URL;
  public static final String DB_CLASS;

  // Application Configuration
  public static final String REGION_NAME;
  public static final String TENANT_NAME;
  public static final String LOG_LEVEL;
  public static final String UPDATE_MODE;
  public static final String CLEANSED_MODE;

  // Environment indicator
  public static final String ENVIRONMENT;
  
  static {

    dotenv = Dotenv.configure()
        .directory(".")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

    logger.info("Loading global environment variables...");

    // API Configuration
    API_SECRET = getEnvWithDefault("API_SECRET", null);
    API_USERNAME = getEnvWithDefault("API_USERNAME", null);
    API_PASSWORD = getEnvWithDefault("API_PASSWORD", null);
    API_URL = getRequiredEnv("API_URL");
    API_VERSION = getEnvWithDefault("API_VERSION", "v1");

    // Database Configuration
    DB_USERNAME = getRequiredEnv("DB_USERNAME");
    DB_PASSWORD = getRequiredEnv("DB_PASSWORD");
    DB_URL = getRequiredEnv("DB_URL");
    DB_CLASS = getEnvWithDefault("DB_CLASS", "com.mysql.cj.jdbc.Driver");

    // Application Configuration
    REGION_NAME = getRequiredEnv("REGION_NAME");
    TENANT_NAME = getRequiredEnv("TENANT_NAME");
    LOG_LEVEL = getEnvWithDefault("LOG_LEVEL", "INFO");
    UPDATE_MODE = getEnvWithDefault("UPDATE_MODE", "false");
    CLEANSED_MODE = getEnvWithDefault("CLEANSED_MODE", "true");
    // Environment indicator
    ENVIRONMENT = getEnvWithDefault("ENVIRONMENT", "development");

    logger.info("Global environment variables loaded successfully");
    logConfiguration();
  }

  // Set in LoadTenant.java
  private static UUID idTenant;

  public static UUID getIdTenant() {
    return idTenant;
  }

  public static void setIdTenant(UUID idTenant) {
    Globals.idTenant = idTenant;
  }

  public static String getTenantName() {
    return TENANT_NAME;
  }
  
  public static String getRegionName() {
    return REGION_NAME;
  }

  /**
   * Get a required environment variable. First checks .env file, then system environment.
   */
  private static String getRequiredEnv(String key) {
    String value = getEnvValue(key);
    if (StringUtils.isEmpty(value)) {
      String message = String.format("Required environment variable '%s' is not set or is empty", key);
      logger.error(message);
      throw new RuntimeException(message);
    }
    return value;
  }
  /**
   * Get an environment variable with a default value if not found.
   */
  private static String getEnvWithDefault(String key, String defaultValue) {
    String value = getEnvValue(key);
    if (StringUtils.isEmpty(value)) {
      logger.debug("Environment variable '{}' not set, using default: '{}'", key, defaultValue);
      return defaultValue;
    }
    return value;
  }
  /**
   * Get environment variable value, checking .env file first, then system environment.
   */
  private static String getEnvValue(String key) {
    // First try .env file, then system environment
    String value = dotenv.get(key);
    if (value == null) {
      value = System.getenv(key);
    }
    return value;
  }
  /**
   * Get an environment variable as an integer with a default value.
   */
  public static int getIntEnv(String key, int defaultValue) {
    String value = getEnvValue(key);
    if (StringUtils.isEmpty(value)) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      logger.warn("Invalid integer value for environment variable '{}': '{}'. Using default: {}", 
          key, value, defaultValue);
      return defaultValue;
    }
  }

  /**
   * Get an environment variable as a boolean with a default value.
   */
  public static boolean getBooleanEnv(String key, boolean defaultValue) {
    String value = getEnvValue(key);
    if (StringUtils.isEmpty(value)) {
      return defaultValue;
    }
    return Boolean.parseBoolean(value);
  }
  /**
   * Check if running in development environment
   */
  public static boolean isDevelopment() {
    return "development".equalsIgnoreCase(ENVIRONMENT) || "dev".equalsIgnoreCase(ENVIRONMENT);
  }

  /**
   * Check if running in production environment
   */
  public static boolean isProduction() {
    return "production".equalsIgnoreCase(ENVIRONMENT) || "prod".equalsIgnoreCase(ENVIRONMENT);
  }

  /**
   * Get batch size as integer
   */
  public static int getBatchSizeInt() {
    return getIntEnv("BATCH_SIZE", 100);
  }

  /**
   * Get max retries as integer
   */
  public static int getMaxRetriesInt() {
    return getIntEnv("MAX_RETRIES", 3);
  }

  /**
   * Get timeout in seconds as integer
   */
  public static int getTimeoutSecondsInt() {
    return getIntEnv("TIMEOUT_SECONDS", 30);
  }

  /**
   * Log the current configuration (excluding sensitive information)
   */
  private static void logConfiguration() {
    if (logger.isInfoEnabled()) {
      logger.info("=== Global Configuration ===");
      logger.info("Environment: {}", ENVIRONMENT);
      logger.info("Region: {}", REGION_NAME);
      logger.info("API URL: {}", API_URL);
      logger.info("API Version: {}", API_VERSION);
      logger.info("Database URL: {}", DB_URL);
      logger.info("Database Class: {}", DB_CLASS);
      logger.info("Log Level: {}", LOG_LEVEL);
      logger.info("Update Mode: {}", UPDATE_MODE);
      logger.info("============================");
    }
  }
  /**
   * Validate that all required environment variables are properly set
   */
  public static void validateConfiguration() {
    logger.info("Validating global configuration...");

    // This will trigger the static block if not already done
    // and throw exceptions for missing required variables

    logger.info("Configuration validation completed successfully");
  }
}