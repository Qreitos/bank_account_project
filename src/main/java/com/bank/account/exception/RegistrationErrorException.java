package com.bank.account.exception;

public class RegistrationErrorException extends RuntimeException {

  public RegistrationErrorException(String message) {
    super(message);
  }
}
