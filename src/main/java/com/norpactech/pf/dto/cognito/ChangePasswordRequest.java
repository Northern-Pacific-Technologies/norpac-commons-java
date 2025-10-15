package com.norpactech.pf.dto.cognito;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  String username;
  String confirmationCode;
  String password;    
}