package com.bank.account.advice;

import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.RegistrationErrorException;
import com.bank.account.model.dto.ErrorResponseDto;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class CustomerExceptionHandler {

  protected final Environment environment;

  @ExceptionHandler(LoginErrorException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorResponseDto handleLoginException(LoginErrorException e) {

    return
        new ErrorResponseDto(environment.getProperty("config.errors.log_in_error"
            + " " + e.getMessage()),
            HttpStatus.NOT_FOUND,
            ZonedDateTime.now());
  }

  @ExceptionHandler(RegistrationErrorException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleRegistrationException(RegistrationErrorException e) {

    return
        new ErrorResponseDto(environment.getProperty("config.errors.register_error"
            + " " + e.getMessage()),
            HttpStatus.NOT_FOUND,
            ZonedDateTime.now());
  }
}
