package com.bank.account.model.dto;

import com.bank.account.model.entity.Account;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerResponseDto {

  private long customerId;
  private String forName;
  private String surName;
  private String birthDate;
  private List<Account> accounts;
}
