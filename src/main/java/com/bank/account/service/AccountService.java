package com.bank.account.service;

import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import java.io.IOException;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

public interface AccountService {

  void createAccount(Customer customer, String accountType);

  Transaction realiseTransaction(Customer customer, String fromIban, String toIban,
      BigDecimal amount, String currency)
      throws IOException, NoSuchMethodException;

  @Transactional
  @Scheduled(fixedDelay = 10000, initialDelay = 10000)
  void savingAccountBalanceUpdate();

  @Transactional
  @Scheduled(fixedDelay = 10000, initialDelay = 10000)
  void investorAccountBalanceUpdate();
}
