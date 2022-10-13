package com.bank.account.model.dto;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ErrorResponseDto {

  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timestamp;
}
