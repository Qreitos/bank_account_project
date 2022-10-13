package com.bank.account.service;

import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import java.io.IOException;
import java.math.BigDecimal;

public interface AccountService {

  void createAccount(Customer customer, String accountType);

  Transaction realiseTransaction(String fromIban, String toIban, BigDecimal amount, String currency)
      throws IOException, NoSuchMethodException;
}
