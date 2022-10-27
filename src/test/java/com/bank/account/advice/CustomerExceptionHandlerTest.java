package com.bank.account.advice;

import static org.junit.jupiter.api.Assertions.*;

import com.bank.account.exception.CreateAccountErrorException;
import com.bank.account.exception.InvalidConfirmationTokenException;
import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.RegistrationErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerExceptionHandlerTest {

  @Autowired
  CustomerExceptionHandler customerExceptionHandler;
  @Test
  void handleLoginException() {
    assertNotNull(
        customerExceptionHandler.handleLoginException(
            new LoginErrorException("/path")));
  }

  @Test
  void handleRegistrationException() {
    assertNotNull(
        customerExceptionHandler.handleRegistrationException(
            new RegistrationErrorException("/path")));
  }

  @Test
  void handleInvalidConfirmationTokenException() {
    assertNotNull(
        customerExceptionHandler.handleInvalidConfirmationTokenException(
            new InvalidConfirmationTokenException("/path")));
  }
}