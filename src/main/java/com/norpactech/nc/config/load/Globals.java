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
  public static final String PARETO_API_CLIENT;
  public static final String PARETO_API_SECRET;
  public static final String PARETO_API_SCOPE;
  
  public static final String PARETO_API_USERNAME;
  public static final String PARETO_API_PASSWORD;
  
  public static final String PARETO_API_URL;
  public static final String PARETO_API_VERSION;

  public static final String PARETO_API_TENANT_NAME;
  public static final String PARETO_API_SCHEMA_NAME;

  // Database Configuration
  public static final String DB_USERNAME;
  public static final String DB_PASSWORD;
  public static final String DB_URL;
  public static final String DB_CLASS;
  public static final String DB_SCHEMA;

  // Application Configuration
  public static final String TENANT_NAME;
  public static final String LOG_LEVEL;
  public static final String UPDATE_MODE;
  public static final String CLEANSED_MODE;
  
  // Loader Configuration
  public static final String IMPORT_DATA_DIRECTORY;

  // Environment indicator
  public static final String ENVIRONMENT;
  
  static {

    dotenv = Dotenv.configure()
        .directory(".")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

    logger.info("Loading global environment variables...");

    PARETO_API_TENANT_NAME = getEnvWithDefault("PARETO_API_TENANT_NAME", null);
    PARETO_API_SCHEMA_NAME = getEnvWithDefault("PARETO_API_SCHEMA_NAME", null);
    
    PARETO_API_URL = getRequiredEnv("PARETO_API_URL");
    PARETO_API_VERSION = getRequiredEnv("PARETO_API_VERSION");

    // Login with Client Credentials only for nopac-commons-api
    PARETO_API_CLIENT = getEnvWithDefault("PARETO_API_CLIENT", null);
    PARETO_API_SECRET = getEnvWithDefault("PARETO_API_SECRET", null);
    PARETO_API_SCOPE = getEnvWithDefault("PARETO_API_SCOPE", "norpac-commons-api/write");

    // Login Username/Password
    PARETO_API_USERNAME = getEnvWithDefault("PARETO_API_USERNAME", null);
    PARETO_API_PASSWORD = getEnvWithDefault("PARETO_API_PASSWORD", null);

    // Loader Configuration
    IMPORT_DATA_DIRECTORY = getEnvWithDefault("IMPORT_DATA_DIRECTORY", "");
    
    // Database Configuration - only used in the Pareto Importer
    DB_USERNAME = getEnvWithDefault("DB_USERNAME", null);
    DB_PASSWORD = getEnvWithDefault("DB_PASSWORD", null);
    DB_URL = getEnvWithDefault("DB_URL", null);
    DB_CLASS = getEnvWithDefault("DB_CLASS", "com.mysql.cj.jdbc.Driver");
    DB_SCHEMA = getEnvWithDefault("DB_SCHEMA", null);

    // Application Configuration used in application loaders
    TENANT_NAME = getEnvWithDefault("TENANT_NAME", null);
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
    
  }
  /**
   * Validate that all required environment variables are properly set
   */
  public static void validateApiConfiguration() {
  
    logger.info("=== API Configuration ===");
    logger.info("API URL.....: {}", PARETO_API_URL);
    logger.info("API Version.: {}", PARETO_API_VERSION);
    logger.info("API Username: {}", PARETO_API_USERNAME);
    logger.info("API Password: {}", PARETO_API_PASSWORD != null ? "********" : "null");

    if (StringUtils.isEmpty(PARETO_API_URL)) {
      logger.error("Null or empty Pareto Factory URL. Set environment variable: PARETO_API_URL. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(PARETO_API_VERSION)) {
      logger.error("Null or empty API Version. Set environment variable: PARETO_API_VERSION. Terminating...");
      System.exit(1);
    }    
    
    if (StringUtils.isEmpty(PARETO_API_USERNAME)) {
      logger.error("Null or empty username. Set environment variable: PARETO_API_USERNAME. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(PARETO_API_PASSWORD)) {
      logger.error("Null or empty password. Set environment variable: PARETO_API_PASSWORD. Terminating...");
      System.exit(1);
    }
  }
  
  public static void validateImportConfiguration() {

    logger.info("=== Import Configuration ===");
    logger.info("Tenant Name: {}", PARETO_API_TENANT_NAME);
    logger.info("Schema Name: {}", PARETO_API_SCHEMA_NAME);

    if (StringUtils.isEmpty(PARETO_API_TENANT_NAME)) {
      logger.error("Null or empty Tenant Name. Set environment variable: PARETO_TENANT_NAME. Terminating...");
      System.exit(1);
    }    
    
    if (StringUtils.isEmpty(PARETO_API_SCHEMA_NAME)) {
      logger.error("Null or empty Schema Name. Set environment variable: PARETO_SCHEMA_NAME. Terminating...");
      System.exit(1);
    }
  }
  
  public static void validateDatabaseConfiguration() {
    
    logger.info("=== API Configuration ===");
    logger.info("DB Username: {}", DB_USERNAME);
    logger.info("DB Password: {}", DB_PASSWORD != null ? "********" : "null");
    logger.info("DB URL.....: {}", DB_URL);
    logger.info("DB Class...: {}", DB_CLASS);
    logger.info("DB Schema..: {}", DB_SCHEMA);

    if (StringUtils.isEmpty(DB_USERNAME)) {
      logger.error("Null or empty Database Username. Set environment variable: DB_USERNAME. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(DB_PASSWORD)) {
      logger.error("Null or empty Database Password. Set environment variable: DB_PASSWORD. Terminating...");
      System.exit(1);
    }    
    
    if (StringUtils.isEmpty(DB_URL)) {
      logger.error("Null or empty Database URL. Set environment variable: DB_URL. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(DB_CLASS)) {
      logger.error("Null or empty Database Class. Set environment variable: DB_CLASS. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(DB_SCHEMA)) {
      logger.error("Null or empty Database Schema. Set environment variable: DB_SCHEMA. Terminating...");
      System.exit(1);
    }
  }
  
  public static void validateLoaderConfiguration() {
    
    logger.info("=== Loader Configuration ===");
    logger.info("Import Data Directory: {}", IMPORT_DATA_DIRECTORY);

    if (StringUtils.isEmpty(IMPORT_DATA_DIRECTORY)) {
      logger.error("Null or empty Import Data Directory. Set environment variable: IMPORT_DATA_DIRECTORY. Terminating...");
      System.exit(1);
    }
  }
  
  public static void validateConfiguration() {
    logger.info("Validating global configuration...");

    // This will trigger the static block if not already done
    // and throw exceptions for missing required variables

    logger.info("Configuration validation completed successfully");
  }
}