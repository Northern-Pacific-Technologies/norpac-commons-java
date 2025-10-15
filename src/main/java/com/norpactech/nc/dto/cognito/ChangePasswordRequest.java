package com.norpactech.nc.dto.cognito;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  String username;
  String confirmationCode;
  String password;    
}