package com.bank.account.service;

import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import java.io.IOException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

public interface AccountService {

  void createAccount(Customer customer, String accountType);

  Transaction realiseTransaction(Customer customer, String fromIban, String toIban,
      BigDecimal amount, String currency)
      throws IOException, NoSuchMethodException;
}
