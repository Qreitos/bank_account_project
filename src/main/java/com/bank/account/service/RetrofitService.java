package com.bank.account.service;

import com.bank.account.model.dto.ApiResponseDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service
public class RetrofitService {

  public BigDecimal getCurrency(String currency) throws IOException, NoSuchMethodException {

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

    Call<ApiResponseDto> call = service.getCurrencies(LocalDate.now());

    Response<ApiResponseDto> response = call.execute();

    ApiResponseDto dto = response.body();

    assert dto != null;
    return BigDecimal.valueOf(dto.eur.get(currency)).setScale(1, RoundingMode.HALF_EVEN);
  }
}
