package com.bank.account.model.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {

  private String forName;
  private String surName;
  private String password;
  private String birthDate;
}
