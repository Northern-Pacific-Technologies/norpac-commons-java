package com.norpactech.nc.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TextUtilsTest {

  @Test
  @DisplayName("toClassName handles snake_case")
  void toClassName_snakeCase() {
    assertEquals("UserName", TextUtils.toClassName("user_name"));
    assertEquals("User", TextUtils.toClassName("user"));
    assertEquals("UserName", TextUtils.toClassName("user_name__"));
    assertEquals("ABC", TextUtils.toClassName("a_b_c"));
  }

  @Test
  @DisplayName("toClassName handles camelCase and PascalCase")
  void toClassName_camelCase() {
    assertEquals("UserName", TextUtils.toClassName("userName"));
    assertEquals("UserName", TextUtils.toClassName("UserName"));
    assertEquals("U", TextUtils.toClassName("u"));
  }

  @Test
  @DisplayName("toClassName handles kebab-case")
  void toClassName_kebabCase() {
    assertEquals("UserName", TextUtils.toClassName("user-name"));
    assertEquals("UserName", TextUtils.toClassName("-user--name-"));
  }

  @Test
  @DisplayName("toClassName handles empty and null")
  void toClassName_emptyNull() {
    assertEquals("", TextUtils.toClassName(""));
    assertNull(TextUtils.toClassName(null));
  }
}
