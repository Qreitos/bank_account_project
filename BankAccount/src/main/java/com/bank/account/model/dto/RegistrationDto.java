package com.bank.account.model.dto;


import java.util.Date;
import lombok.Data;

@Data
public class RegistrationDto {

  private String forName;
  private String surName;
  private String password;
  private Date birthDate;
}
