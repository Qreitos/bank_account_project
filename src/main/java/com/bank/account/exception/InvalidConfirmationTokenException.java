package com.bank.account.exception;

public class InvalidConfirmationTokenException extends RuntimeException {

  public InvalidConfirmationTokenException(String message) {
    super(message);
  }
}
