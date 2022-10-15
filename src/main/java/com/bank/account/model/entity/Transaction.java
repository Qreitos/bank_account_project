package com.bank.account.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
  @JsonIgnore
  @ManyToMany
  private List<Customer> customers;

  public Transaction(Customer customer, String fromIban, String toIban,
      BigDecimal amount, String status) {
    this.fromIban = fromIban;
    this.toIban = toIban;
    this.amount = amount;
    this.realisationDate = ZonedDateTime.now();
    this.status = status;
    customers.add(customer);
  }
}
