package com.bank.account.model.entity;

import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class Transaction {

  private String iban;
  private ZonedDateTime realisationDate;
  private String status;
}
