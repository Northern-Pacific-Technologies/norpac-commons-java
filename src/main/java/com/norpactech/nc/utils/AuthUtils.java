package com.norpactech.nc.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.norpactech.nc.enums.EnumStatus;
import com.norpactech.nc.vo.JwtClientCredentialsRequestVO;
import com.norpactech.nc.vo.JwtUsernamePasswordRequestVO;

import okhttp3.OkHttpClient;

public class AuthUtils {

  private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

  public static String getJwt(String tokenUrl, JwtUsernamePasswordRequestVO jwtRequest) throws Exception {

    URL url = new URL(tokenUrl);
    okhttp3.Response response = null;

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    okhttp3.FormBody.Builder formBuilder = new okhttp3.FormBody.Builder()
        .add("username", jwtRequest.getEmail())
        .add("password", jwtRequest.getPassword());
    
    okhttp3.FormBody formBody = formBuilder.build();
    okhttp3.Request request = new okhttp3.Request.Builder()
        .url(url)
        .post(formBody)
        .addHeader("Accept", "*/*")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();

    response = client.newCall(request).execute();     
    JsonObject jsonObject = new Gson().fromJson(new String(response.body().bytes()), JsonObject.class);

    if (jsonObject.has("status")) {
      String status = jsonObject.get("status").getAsString();
      if (status.equals(EnumStatus.ERROR.getName())) {
        String error = jsonObject.get("error").getAsString();
        throw new Exception("Sign In Failed: " + error); 
      }
    }
    JsonElement token = jsonObject.get("access_token");
    logger.info("Access Token successfully retrieved");
    return token.toString().replace("\"", "");
  }
  
  public static String getJwt(String tokenUrl, JwtClientCredentialsRequestVO jwtRequest) throws Exception {

    if (jwtRequest == null) {
      throw new IllegalArgumentException("JwtRequestVO cannot be null");
    }

    if (jwtRequest.getClientId() == null || jwtRequest.getClientId().isEmpty()) {
      throw new IllegalArgumentException("Missing client_id for AWS Cognito request");
    }

    if (jwtRequest.getClientSecret() == null || jwtRequest.getClientSecret().isEmpty()) {
      throw new IllegalArgumentException("Missing client_secret for AWS Cognito request");
    }

    // Build Basic Auth header
    String clientCredentials = jwtRequest.getClientId() + ":" + jwtRequest.getClientSecret();
    String encodedCredentials = Base64.getEncoder()
        .encodeToString(clientCredentials.getBytes(StandardCharsets.UTF_8));
    String authorizationHeader = "Basic " + encodedCredentials;

    // Form body for AWS Cognito token request
    okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
        .add("grant_type", "client_credentials")
        .build();

    // Prepare HTTP client and request
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    okhttp3.Request request = new okhttp3.Request.Builder()
        .url(tokenUrl)
        .post(formBody)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Authorization", authorizationHeader)
        .build();

    // Execute call
    try (okhttp3.Response response = client.newCall(request).execute()) {

      String bodyStr = response.body() != null ? response.body().string() : "";

      if (!response.isSuccessful()) {
        logger.error("AWS Token request failed: HTTP "
            + response.code() + " - " + response.message() + " : " + bodyStr);
        
        throw new Exception("AWS Token request failed: HTTP "
            + response.code() + " - " + response.message() + " : " + bodyStr);
      }

      // Parse JSON
      JsonObject jsonObject = new Gson().fromJson(bodyStr, JsonObject.class);
      if (jsonObject == null || !jsonObject.has("access_token")) {
        throw new Exception("Invalid AWS token response: " + bodyStr);
      }

      if (jsonObject.has("error")) {
        String err = jsonObject.get("error").getAsString();
        String desc = jsonObject.has("error_description") 
            ? jsonObject.get("error_description").getAsString() : "";
        throw new Exception("AWS Cognito Sign In Failed: " + err + 
            (desc.isEmpty() ? "" : " - " + desc));
      }

      String accessToken = jsonObject.get("access_token").getAsString();
      logger.info("AWS Client Credential Token successfully retrieved");

      return accessToken;
    }
  }
  
  
  
  
}