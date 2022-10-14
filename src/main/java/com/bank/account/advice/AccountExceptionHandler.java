package com.bank.account.advice;

import com.bank.account.exception.CreateAccountErrorException;
import com.bank.account.exception.TransactionErrorException;
import com.bank.account.model.dto.ErrorResponseDto;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class AccountExceptionHandler {

  protected final Environment environment;

  @ExceptionHandler(CreateAccountErrorException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponseDto handleCreateAccountException(CreateAccountErrorException e) {

    return
        new ErrorResponseDto(environment.getProperty("config.errors.create_account_error")
            + " " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ZonedDateTime.now());
  }

  @ExceptionHandler(TransactionErrorException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleTransactionErrorException(TransactionErrorException e) {

    return
        new ErrorResponseDto(environment.getProperty("config.errors.transaction_error")
            + " " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            ZonedDateTime.now());
  }
}
