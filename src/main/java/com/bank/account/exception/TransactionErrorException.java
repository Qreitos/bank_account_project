package com.bank.account.exception;

public class TransactionErrorException extends RuntimeException{

  public TransactionErrorException(String message) {
    super(message);
  }
}
