package com.bank.account.model.dto;

import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;

@Data
public class ApiResponseDto {

  public String date;
  public LinkedTreeMap<String, Double> eur;
}
