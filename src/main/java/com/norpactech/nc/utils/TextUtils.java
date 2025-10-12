package com.norpactech.nc.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 * 
 * For details, see the LICENSE file in this project root.
 */
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
/**
 * Utility class providing common text manipulation and conversion methods.
 * This class contains static methods for string processing, case conversion,
 * type conversion, and SQL escaping operations.
 * 
 * @author Northern Pacific Technologies, LLC
 * @since 1.0.0
 */
public class TextUtils {

  /**
   * Escapes single quotes in SQL strings by doubling them.
   * This method helps prevent SQL injection by properly escaping single quotes.
   * 
   * @param sql the SQL string to escape, may be null
   * @return the escaped SQL string, or null if input is null
   * @example escapeSql("SELECT * FROM users WHERE name = 'O'Brien'") 
   *          returns "SELECT * FROM users WHERE name = 'O''Brien'"
   */
  public static String escapeSql(String sql) {

    if (sql == null) {
      return null;
    }

    StringBuilder escapedText = new StringBuilder();
    for (char c : sql.toCharArray()) {
      if (c == '\'') {
        escapedText.append("''");
      } 
      else {
        escapedText.append(c);
      }
    }
    return escapedText.toString();
  }  
  /**
   * Converts a string to a UUID object.
   * 
   * @param source the string representation of a UUID, may be null or blank
   * @return the UUID object, or null if source is null or blank
   * @throws RuntimeException if the source string is not a valid UUID format
   * @example toUUID("550e8400-e29b-41d4-a716-446655440000") returns a UUID object
   */
  public static UUID toUUID(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    
    try {
      return UUID.fromString(source);
    } 
    catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid UUID: " + source);
    }
  }    
  /**
   * Removes the "id_" prefix from foreign key field names.
   * This method is useful for converting database foreign key column names
   * to more readable field names.
   * 
   * @param source the source string, may be null
   * @return the string with "id_" prefix removed and trimmed, or null if input is null
   * @example fkName("id_user") returns "user"
   * @example fkName("user_name") returns "user_name"
   */
  public static String fkName(String source) {

    if (source == null) {
      return null;
    }
    
    String retVal = source.trim();
    if (source.startsWith("id_")) {
      retVal = source.substring(3);
    }
    return retVal;
  }
  /**
   * Trims whitespace from a string and returns null for blank strings.
   * 
   * @param source the source string, may be null or blank
   * @return the trimmed string, or null if source is null or blank
   * @example toString("  hello  ") returns "hello"
   * @example toString("   ") returns null
   */
  public static String toString(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    return source.trim();
  }
  /**
   * Converts a string to an Integer object.
   * 
   * @param source the string representation of an integer, may be null or blank
   * @return the Integer object, or null if source is null or blank
   * @throws RuntimeException if the source string is not a valid integer format
   * @example toInteger("123") returns Integer(123)
   * @example toInteger("abc") throws RuntimeException
   */
  public static Integer toInteger(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    try {
      return Integer.parseInt(source);
    } 
    catch (NumberFormatException e) {
      throw new RuntimeException("Invalid integer: " + source);
    }
  }
  /**
   * Converts a string to a Boolean object.
   * Only accepts "true" or "false" (case-insensitive).
   * 
   * @param source the string representation of a boolean, may be null or blank
   * @return the Boolean object, or null if source is null or blank
   * @throws RuntimeException if the source string is not "true" or "false"
   * @example toBoolean("TRUE") returns Boolean.TRUE
   * @example toBoolean("false") returns Boolean.FALSE
   * @example toBoolean("yes") throws RuntimeException
   */
  public static Boolean toBoolean(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }

    String value = source.toLowerCase();
    if (value.equals("true")) {
      return true;
    }
    else if (value.equals("false")) {
      return false;
    }
    else {
      throw new RuntimeException("Invalid boolean: " + source);
    }
  }
  /**
   * Converts snake_case strings to camelCase.
   * Underscores are removed and the following character is capitalized.
   * 
   * @param snakeCase the snake_case string to convert, may be null or empty
   * @return the camelCase string, or the original string if null or empty
   * @example toCamelCase("user_name") returns "userName"
   * @example toCamelCase("first_name_last") returns "firstNameLast"
   */
  public static String toCamelCase(String snakeCase) {

    if (snakeCase == null || snakeCase.isEmpty()) {
      return snakeCase;
    }
    StringBuffer camelCase = new StringBuffer();
    boolean nextCharUpperCase = false;

    for (char c : snakeCase.toCharArray()) {
      if (c == '_') {
        nextCharUpperCase = true;
      } else {
        if (nextCharUpperCase) {
          camelCase.append(Character.toUpperCase(c));
          nextCharUpperCase = false;
        } else {
          camelCase.append(c);
        }
      }
    }
    return camelCase.toString();
  }  
  /**
   * Converts camelCase strings to snake_case.
   * Uppercase characters are converted to lowercase and preceded by an underscore.
   * Leading underscores are removed from the result.
   * 
   * @param camelCase the camelCase string to convert, may be null or empty
   * @return the snake_case string, or the original string if null or empty
   * @example toSnakeCase("userName") returns "user_name"
   * @example toSnakeCase("FirstName") returns "first_name"
   */
  public static String toSnakeCase(String camelCase) {

    if (camelCase == null || camelCase.isEmpty()) {
      return camelCase;
    }
    StringBuffer snakeCase = new StringBuffer();
    for (char c : camelCase.toCharArray()) {
      if (Character.isUpperCase(c)) {
        snakeCase.append('_');
        snakeCase.append(Character.toLowerCase(c));
      } else {
        snakeCase.append(c);
      }
    }
    String retVal = snakeCase.toString();
    if (retVal.startsWith("_")) {
      retVal = retVal.substring(1);
    }
    return retVal;
  }
  /**
   * Converts camelCase strings to kebab-case.
   * Uppercase characters are converted to lowercase and preceded by a hyphen.
   * Leading hyphens are removed from the result.
   * 
   * @param camelCase the camelCase string to convert, may be null or empty
   * @return the kebab-case string, or the original string if null or empty
   * @example toKebabCase("userName") returns "user-name"
   * @example toKebabCase("FirstName") returns "first-name"
   */
  public static String toKebabCase(String camelCase) {

    if (camelCase == null || camelCase.isEmpty()) {
      return camelCase;
    }
    StringBuffer kebabCase = new StringBuffer();
    for (char c : camelCase.toCharArray()) {
      if (Character.isUpperCase(c)) {
        kebabCase.append('-');
        kebabCase.append(Character.toLowerCase(c));
      } else {
        kebabCase.append(c);
      }
    }
    String retVal = kebabCase.toString();
    if (retVal.startsWith("-")) {
      retVal = retVal.substring(1);
    }
    return retVal;
  }
  /**
   * Extracts the simple class name from a fully qualified class name.
   * Returns everything after the last dot in the class name.
   * 
   * @param fullClassName the fully qualified class name, may be null or empty
   * @return the simple class name, or the original string if no package separator found
   * @example toShortClassName("com.example.User") returns "User"
   * @example toShortClassName("User") returns "User"
   */
  public static String toShortClassName(String fullClassName) {

    if (fullClassName == null || fullClassName.isEmpty()) {
      return fullClassName;
    }
    int lastDotIndex = fullClassName.lastIndexOf('.');
    if (lastDotIndex == -1) {
      return fullClassName;
    }
    return fullClassName.substring(lastDotIndex + 1);
  }  
  /**
   * Generates a getter method name from a field name.
   * Capitalizes the first letter and prefixes with "get".
   * 
   * @param shortClassName the field name, may be null or empty
   * @return the getter method name, or the original string if null or empty
   * @example toGetter("name") returns "getName"
   * @example toGetter("isActive") returns "getIsActive"
   */
  public static String toGetter(String shortClassName) {

    if (shortClassName == null || shortClassName.isEmpty()) {
      return shortClassName;
    }
    return "get" + shortClassName.substring(0, 1).toUpperCase() + shortClassName.substring(1);
  } 
  /**
   * Generates a setter method name from a field name.
   * Capitalizes the first letter and prefixes with "set".
   * 
   * @param shortClassName the field name, may be null or empty
   * @return the setter method name, or the original string if null or empty
   * @example toSetter("name") returns "setName"
   * @example toSetter("isActive") returns "setIsActive"
   */
  public static String toSetter(String shortClassName) {

    if (shortClassName == null || shortClassName.isEmpty()) {
      return shortClassName;
    }
    return "set" + shortClassName.substring(0, 1).toUpperCase() + shortClassName.substring(1);
  } 
  /**
   * Converts a map of parameters to a URL query string.
   * Parameters are joined with '&amp;' and prefixed with '?'.
   * 
   * @param params the map of parameter key-value pairs, may be null or empty
   * @return the query string (e.g., "?key1=value1&amp;key2=value2"), or empty string if no parameters
   * @example toQueryString(Map.of("name", "john", "age", "25")) returns "?name=john&amp;age=25"
   */
  public static String toQueryString(Map<String, String> params) {

    StringBuilder queryString = new StringBuilder();
    if (params != null && !params.isEmpty()) {
      queryString.append("?");
      for (Map.Entry<String, String> entry : params.entrySet()) {
        queryString.append(entry.getKey())
        .append("=")
        .append(entry.getValue())
        .append("&");
      }
      queryString.setLength(queryString.length() - 1);
      return queryString.toString();
    }
    return "";
  }
  /**
   * Extracts the substring after the last occurrence of a delimiter.
   * 
   * @param fullValue the full string to search within
   * @param delimeter the delimiter string to search for
   * @return the substring after the last occurrence of the delimiter
   * @throws StringIndexOutOfBoundsException if delimiter is not found
   * @example lastDelimetedValue("com.example.User", ".") returns "User"
   * @example lastDelimetedValue("/path/to/file.txt", "/") returns "file.txt"
   */
  public static String lastDelimetedValue(String fullValue, String delimeter) {
    return fullValue.substring(fullValue.lastIndexOf(delimeter) + 1);
  }  

  /**
   * Converts either snake_case or camelCase text into a Java class name (UpperCamelCase).
   * Underscores or hyphens are treated as word delimiters when present; otherwise,
   * the first character is simply uppercased, preserving existing camel humps.
   *
   * @param text the input text in snake_case or camelCase, may be null or empty
   * @return the converted Java class name, or the original string if null or empty
   * @example toClassName("user_name") returns "UserName"
   * @example toClassName("userName") returns "UserName"
   * @example toClassName("UserName") returns "UserName"
   */
  public static String toClassName(String text) {
    if (text == null || text.isEmpty()) {
      return text;
    }

    // If it looks like delimited words (snake/kebab), split and capitalize each token
    if (text.indexOf('_') >= 0 || text.indexOf('-') >= 0) {
      String[] parts = text.split("[_\\-]+");
      StringBuilder sb = new StringBuilder();
      for (String part : parts) {
        if (part.isEmpty()) continue;
        if (part.length() == 1) {
          sb.append(Character.toUpperCase(part.charAt(0)));
        } else {
          sb.append(Character.toUpperCase(part.charAt(0)))
            .append(part.substring(1).toLowerCase());
        }
      }
      return sb.toString();
    }

    // Otherwise assume camelCase or already PascalCase; just uppercase the first char
    if (text.length() == 1) {
      return text.toUpperCase();
    }
    return Character.toUpperCase(text.charAt(0)) + text.substring(1);
  }
}