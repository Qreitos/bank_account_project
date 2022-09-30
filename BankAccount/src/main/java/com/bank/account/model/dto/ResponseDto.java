package com.bank.account.model.dto;

import java.time.ZonedDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {

  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime responseDate;
  private final String accessToken;


}
