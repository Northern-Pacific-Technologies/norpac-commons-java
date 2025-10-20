package com.norpactech.nc.dto.cognito;
import lombok.Data;

@Data
public class ClientCredentialRequest {

  String clientSecret;
  String scope;
}