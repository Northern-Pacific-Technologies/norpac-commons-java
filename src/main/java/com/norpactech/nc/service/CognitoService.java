package com.norpactech.nc.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.norpactech.nc.api.utils.ApiResponse;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserSettingsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GlobalSignOutRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MFAOptionType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ResendConfirmationCodeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

public class CognitoService {

  protected String region = null;
  protected String userPoolId = null;
  protected String userPoolClientId = null;
  protected String userPoolClientSecret = null;

  public void init(
      String region, 
      String userPoolId, 
      String userPoolClientId, 
      String userPoolClientSecret) {
    
    this.region = region;
    this.userPoolId = userPoolId;
    this.userPoolClientId = userPoolClientId;
    this.userPoolClientSecret = userPoolClientSecret;
  }
  
  public ApiResponse signUp(String username, String password) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region)) 
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      SignUpRequest signUpRequest = SignUpRequest.builder()
          .clientId(userPoolClientId)
          .username(username)
          .password(password)
          .secretHash(calculateSecretHash(username))
          .build();

      cognitoClient.signUp(signUpRequest);
      return new ApiResponse("User registered successfully"); 
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }

  public ApiResponse confirmSignUp(String username, String confirmationCode) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      ConfirmSignUpRequest confirmRequest = ConfirmSignUpRequest.builder()
          .clientId(userPoolClientId)
          .username(username)
          .confirmationCode(confirmationCode)
          .secretHash(calculateSecretHash(username))
          .build();

      cognitoClient.confirmSignUp(confirmRequest);
      return new ApiResponse("User confirmed successfully");
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }


  public ApiResponse signIn(String username, String password) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
          .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
          .clientId(userPoolClientId)
          .authParameters(Map.of(
              "USERNAME", username,
              "PASSWORD", password,
              "SECRET_HASH", calculateSecretHash(username)
              ))
          .build();

      InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);

      if (authResponse.challengeName() != null && authResponse.challengeName().equals(ChallengeNameType.SOFTWARE_TOKEN_MFA.toString())) {
        String session = authResponse.session();
        ApiResponse response = new ApiResponse("Two Factor Authentication Required");
        response.setData(session);
        return response;
      }
      // Extract tokens
      String accessToken = authResponse.authenticationResult().accessToken();
      String refreshToken = authResponse.authenticationResult().refreshToken();
      String idToken = authResponse.authenticationResult().idToken();

      // Return all tokens in the response
      Map<String, String> tokens = Map.of(
          "accessToken", accessToken,
          "refreshToken", refreshToken,
          "idToken", idToken
          );
      return new ApiResponse(tokens);
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }
  
  public ApiResponse refreshToken(String refreshToken) {
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
          .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
          .clientId(userPoolClientId)
          .authParameters(Map.of(
              "REFRESH_TOKEN", refreshToken
              ))
          .build();

      InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);

      String accessToken = authResponse.authenticationResult().accessToken();
      String idToken = authResponse.authenticationResult().idToken();

      Map<String, String> tokens = Map.of(
          "accessToken", accessToken,
          "idToken", idToken
          );

      return new ApiResponse(tokens);
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }

  public ApiResponse forgotPassword(String username) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
          .clientId(userPoolClientId)
          .username(username)
          .secretHash(calculateSecretHash(username)) 
          .build();

      cognitoClient.forgotPassword(forgotPasswordRequest);
      return new ApiResponse("Password reset initiated. Check your email for the verification code.");
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }

  public ApiResponse changePassword(String username, String confirmationCode, String password) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
          .clientId(userPoolClientId)
          .username(username)
          .confirmationCode(confirmationCode)
          .password(password)
          .secretHash(calculateSecretHash(username)) 
          .build();

      cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);
      return new ApiResponse("Password changed successfully.");
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }
  
  public ApiResponse resendCode(String username) {

    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      ResendConfirmationCodeRequest resendRequest = ResendConfirmationCodeRequest.builder()
          .clientId(userPoolClientId)
          .username(username)
          .secretHash(calculateSecretHash(username)) 
          .build();

      cognitoClient.resendConfirmationCode(resendRequest);
      return new ApiResponse("Verification code sent successfully.");
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }
  
  // TODO: Make this work! The User Pool Id is not being found
  public ApiResponse enableMfa(String username) {

    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      MFAOptionType mfaOption = MFAOptionType.builder()
          .deliveryMedium("EMAIL")
          .attributeName("email")
          .build();

      AdminSetUserSettingsRequest request = AdminSetUserSettingsRequest.builder()
          .username(username)
          .userPoolId(userPoolId)
          .mfaOptions(mfaOption)
          .build();

      cognitoClient.adminSetUserSettings(request);
      return new ApiResponse("MFA enabled for user: " + username);
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }

  public ApiResponse sendMfaCode(String username, String password) {

    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
          .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
          .clientId(userPoolClientId)
          .authParameters(Map.of(
              "USERNAME", username,
              "PASSWORD", password
              ))
          .build();

      InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
      String session = authResponse.session();
      return new ApiResponse(session);
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }
  
  public ApiResponse signOut(String accessToken) {
    
    try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()) {

      GlobalSignOutRequest signOutRequest = GlobalSignOutRequest.builder()
          .accessToken(accessToken)
          .build();

      cognitoClient.globalSignOut(signOutRequest);
      return new ApiResponse("User signed out successfully");
    } 
    catch (CognitoIdentityProviderException e) {
      return new ApiResponse(e);
    }
  }  

  private String calculateSecretHash(String username) {
    try {
      String message = username + userPoolClientId;
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(userPoolClientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      mac.init(secretKeySpec);
      byte[] digest = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(digest);
    } 
    catch (Exception e) {
      throw new RuntimeException("Failed to calculate SECRET_HASH", e);
    }
  }  
}