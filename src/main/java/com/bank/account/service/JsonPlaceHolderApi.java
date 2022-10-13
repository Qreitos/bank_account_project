package com.bank.account.service;

import com.bank.account.model.dto.ApiResponseDto;
import java.time.LocalDate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

  @GET("{date}/currencies/eur.json")
  Call<ApiResponseDto> getCurrencies(@Path("date") LocalDate date);
}
