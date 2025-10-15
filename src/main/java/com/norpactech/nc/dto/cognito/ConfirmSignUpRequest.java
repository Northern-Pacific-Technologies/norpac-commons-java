package com.norpactech.nc.dto.cognito;
import lombok.Data;

@Data
public class ConfirmSignUpRequest {

  String username;
  String confirmationCode;
}