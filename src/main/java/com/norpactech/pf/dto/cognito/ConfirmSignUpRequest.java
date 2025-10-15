package com.norpactech.pf.dto.cognito;
import lombok.Data;

@Data
public class ConfirmSignUpRequest {

  String username;
  String confirmationCode;
}