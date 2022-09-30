package com.bank.account.model.dto;

import lombok.Data;

@Data
public class LoginDto {
  private int loginNumber;
  private String password;
}
