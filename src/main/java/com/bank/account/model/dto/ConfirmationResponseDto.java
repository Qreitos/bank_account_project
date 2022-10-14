package com.bank.account.model.dto;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ConfirmationResponseDto {
  private String status;
  private final HttpStatus httpStatus;
  private final ZonedDateTime responseDate;
}
