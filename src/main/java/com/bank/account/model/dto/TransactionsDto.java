package com.bank.account.model.dto;

import com.bank.account.model.entity.Transaction;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionsDto {

  List<Transaction> transactions;
}
