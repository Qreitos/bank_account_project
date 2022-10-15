package com.bank.account.model.entity;

import com.bank.account.model.dto.TransactionsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long customerId;
  private int loginNumber;
  private String forName;
  private String surName;
  private String password;
  private String birthDate;
  private String email;
  @JsonIgnore
  private boolean enabled = false;
  @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  private List<Account> accounts;
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles;
  @JsonIgnore
  @ManyToMany(mappedBy = "customer", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  private List<Transaction> transactions;

  public void addAccount(Account account) {
    accounts.add(account);
  }

  public void addTransaction(Transaction transaction) {
    this.transactions.add(transaction);
  }
}
