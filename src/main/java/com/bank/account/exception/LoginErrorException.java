package com.bank.account.exception;

public class LoginErrorException extends RuntimeException {

  public LoginErrorException(String message) {
    super(message);
  }
}
