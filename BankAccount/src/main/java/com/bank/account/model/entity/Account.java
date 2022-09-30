package com.bank.account.model.entity;

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
  private long accountNumber;
  private String iban = "SK45 0120 0000 00" + String.valueOf(accountNumber);
  @ManyToOne
  private Customer customer;

  public Account(Customer customer) {
    int firstFiveDigits = (int)(Math.random() * 100000) + 10000;
    int secondFiveDigits = (int)(Math.random() * 100000) + 10000;


    this.customer = customer;
  }
}
