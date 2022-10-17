package com.bank.account.service;

import com.bank.account.enums.AccountType;
import com.bank.account.model.entity.Account;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.repository.TransactionRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;
  private final TransactionRepository transactionRepository;
  private final RetrofitService retrofitService;

  @Override
  public void createAccount(Customer customer, String accountType) {
    log.info("Creating new Account");

    Account account = new Account(customer);

    if (accountType.equals("CLASSIC")) {
      account.setAccountType(AccountType.CLASSIC);
    }
    if (accountType.equals("SAVING")) {
      account.setAccountType(AccountType.SAVING);
    }
    if (accountType.equals("INVESTOR")) {
      account.setAccountType(AccountType.INVESTOR);
    }

    customer.addAccount(account);
    customerRepository.save(customer);
    accountRepository.save(account);
  }

  @Override
  public Transaction realiseTransaction(Customer customer, String fromIban,
      String toIban, BigDecimal amount,
      String currency) throws IOException, NoSuchMethodException {

    Account fromAccount = accountRepository.getAccountByIban(fromIban);
    Account toAccount = accountRepository.getAccountByIban(toIban);

    BigDecimal currencyRate = retrofitService.getCurrency(currency);
    BigDecimal amountInEur = amount.divide(currencyRate, RoundingMode.HALF_EVEN);

    log.info("Creating transaction");

    if (fromAccount != null && toAccount != null) {
      fromAccount.setBalance(fromAccount.getBalance().subtract(amountInEur));
      toAccount.setBalance(toAccount.getBalance().add(amountInEur));
      Transaction transaction =
          new Transaction(customer, fromIban, toIban, amount, "Realised");
      customer.addTransaction(transaction);
      customerRepository.save(customer);
      accountRepository.save(fromAccount);
      accountRepository.save(toAccount);

      transaction.setCurrency(currency.toUpperCase());
      transactionRepository.save(transaction);

      log.info("Transaction realised");
      return transaction;
    }
    Transaction transaction =
        new Transaction(customer, fromIban, toIban, amount, "Not realised");
    transaction.setCurrency(currency.toUpperCase());
    customer.addTransaction(transaction);
    transactionRepository.save(transaction);
    customerRepository.save(customer);

    log.info("Transaction not realised");
    return transaction;
  }
}
