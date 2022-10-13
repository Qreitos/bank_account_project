package com.bank.account.model.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String fromIban;
  private String toIban;
  private BigDecimal amount;
  private String currency;
  private ZonedDateTime realisationDate;
  private String status;

  public Transaction(String fromIban, String toIban, BigDecimal amount, String status) {
    this.fromIban = fromIban;
    this.toIban = toIban;
    this.amount = amount;
    this.realisationDate = ZonedDateTime.now();
    this.status = status;
  }
}
