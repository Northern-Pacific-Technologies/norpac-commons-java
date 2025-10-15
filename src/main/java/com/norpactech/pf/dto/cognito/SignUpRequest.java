package com.norpactech.pf.dto.cognito;
import lombok.Data;

@Data
public class SignUpRequest {

  String username;
  String password;
}