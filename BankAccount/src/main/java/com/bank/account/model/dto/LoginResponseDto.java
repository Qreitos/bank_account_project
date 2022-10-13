package com.bank.account.model.dto;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class LoginResponseDto {

  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime responseDate;
  private String accessToken;
}
