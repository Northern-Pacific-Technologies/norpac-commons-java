package com.norpactech.pf.dto.cognito;
import lombok.Data;

@Data
public class SignInRequest {

  String username;
  String password;
}