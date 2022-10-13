package com.bank.account.exception;

public class CreateAccountErrorException extends RuntimeException {

  public CreateAccountErrorException(String message) {
    super(message);
  }
}
