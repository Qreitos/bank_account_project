package com.bank.account.service;

import com.bank.account.model.entity.Customer;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.repository.TransactionRepository;
import java.io.IOException;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {

  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  public void can_create_account_test() {
    accountRepository.deleteAll();
    customerRepository.deleteAll();
    Customer customer = new Customer();
    accountService.createAccount(customer, "INVESTOR");
    Assertions.assertEquals(1, accountRepository.findAll().size());
  }

  @Test
  @Transactional
  public void can_realise_transaction_test() throws IOException, NoSuchMethodException {
    transactionRepository.deleteAll();
    Customer customer = new Customer();
    accountService.realiseTransaction(customer, "SK45 0120 0000 0031 8920 6398",
        "SK45 0120 0000 0050 8578 8292", BigDecimal.valueOf(100), "eur");

    Assertions.assertEquals("Not realised",
        transactionRepository.getTransactionById(1).getStatus());
  }
}
