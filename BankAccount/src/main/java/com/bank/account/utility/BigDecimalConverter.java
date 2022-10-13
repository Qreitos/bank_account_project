package com.bank.account.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.Data;

@Converter
@Data
public class BigDecimalConverter implements AttributeConverter<BigDecimal, Long> {

  public Long convertToDatabaseColumn(BigDecimal value) {
    return value == null ? null : value.multiply(BigDecimal.valueOf(100L)).longValue();
  }

  public BigDecimal convertToEntityAttribute(Long value) {
    return value == null ? null
        : (new BigDecimal(value)).divide(BigDecimal.valueOf(100L), 1, RoundingMode.HALF_EVEN);
  }
}