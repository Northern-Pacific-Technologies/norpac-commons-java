package com.norpactech.nc.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.net.URL;

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

    URL url = new URL(tokenUrl);
    okhttp3.Response response = null;

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    okhttp3.FormBody.Builder formBuilder = new okhttp3.FormBody.Builder()
        .add("secret", jwtRequest.getClientSecret())
        .add("scope", jwtRequest.getScope());
    
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
    JsonObject data = jsonObject.getAsJsonObject("data");
    String accessToken = data != null && data.has("accessToken")
        ? data.get("accessToken").getAsString()
        : null;    
    logger.info("Access Token successfully retrieved");
    return accessToken;
  }
}