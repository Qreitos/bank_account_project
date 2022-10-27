package com.bank.account.advice;

import static org.junit.jupiter.api.Assertions.*;

import com.bank.account.exception.CreateAccountErrorException;
import com.bank.account.exception.TransactionErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountExceptionHandlerTest {

  @Autowired
  AccountExceptionHandler accountExceptionHandler;

  @Test
  void handleCreateAccountException() {
    assertNotNull(
        accountExceptionHandler.handleCreateAccountException(
            new CreateAccountErrorException("/path")));
  }

  @Test
  void handleTransactionErrorException() {
    assertNotNull(
        accountExceptionHandler.handleTransactionErrorException(
            new TransactionErrorException("/path")));
  }
}