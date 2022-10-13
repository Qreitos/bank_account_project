package com.bank.account.model.entity;

import com.bank.account.enums.AccountType;
import com.bank.account.utility.BigDecimalConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long accountId;
  private AccountType accountType;
  private LocalDate accountCreationDate;
  private Long accountNumber;
  private String iban;
  @Convert(converter = BigDecimalConverter.class)
  private BigDecimal balance = BigDecimal.valueOf(0);
  private String currency = "EUR";
  @ManyToOne
  @JsonIgnore
  private Customer customer;

  public Account(Customer customer) {
    this.customer = customer;
    this.accountCreationDate = LocalDate.now();
    this.accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

    String accountNumberFormat = String.valueOf(this.accountNumber);
    iban = "SK45 0120 0000 00";
    char[] accountNumberArray = accountNumberFormat.toCharArray();
    StringBuilder builder = new StringBuilder();
    builder.append(iban);
    for (int i = 0; i < accountNumberArray.length; i++) {
      builder.append(accountNumberArray[i]);
      if (i == 1 || i == 5) {
        builder.append(" ");
      }
    }
    this.iban = builder.toString();
  }
}
