package com.bank.account.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.bank.account.exception.CreateAccountErrorException;
import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.TransactionErrorException;
import com.bank.account.model.dto.AccountCreateResponseDto;
import com.bank.account.model.dto.CustomerResponseDto;
import com.bank.account.model.dto.TransactionsDto;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import com.bank.account.service.AccountService;
import com.bank.account.service.CustomerService;
import com.bank.account.service.RetrofitService;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class HomeController {

  private final CustomerService customerService;
  private final AccountService accountService;
  private final RetrofitService retrofitService;


  @RequestMapping(value = "/home", method = GET)
  public ResponseEntity<CustomerResponseDto> getHomeWithCurrency(
      @RequestHeader(name = "Authorization") String token,
      @RequestParam(name = "currency") String currency) {

    if (token == null || currency == null) {
      throw new LoginErrorException("Not provided required data");
    }

    String base64Token = token.replace(token.substring(0, 7), "");

    CustomerResponseDto customerResponseDto = customerService.transferCustomerToDto(
        customerService.getCustomerFromToken(base64Token));

    customerResponseDto.getAccounts().forEach(account -> {
      try {
        account.setBalance(account.getBalance()
            .multiply(retrofitService.getCurrency(currency)));
        account.setCurrency(currency.toUpperCase());
      } catch (IOException | NoSuchMethodException e) {
        throw new LoginErrorException(e.getMessage());
      }
    });
    return ResponseEntity.ok().body(customerResponseDto);
  }

  @RequestMapping(value = "/create/account", method = PUT)
  public ResponseEntity<AccountCreateResponseDto> createAccount(
      @RequestHeader(name = "Authorization") String token,
      @RequestParam(name = "accountType") String accountType) {

    try {
      String base64Token = token.replace(token.substring(0, 7), "");
      accountService.createAccount(customerService.getCustomerFromToken(base64Token),
          accountType.toUpperCase());
    } catch (Exception e) {
      throw new CreateAccountErrorException(e.getMessage());
    }
    return ResponseEntity.ok().body(
        new AccountCreateResponseDto("Account created",
            HttpStatus.ACCEPTED,
            ZonedDateTime.now()));
  }

  @RequestMapping(value = "/transaction", method = POST)
  public ResponseEntity<Transaction> realiseTransaction(
      @RequestHeader(name = "Authorization") String token,
      @RequestParam(name = "currency") String currency,
      @RequestBody Transaction transaction) {

    Transaction newTransaction;
    try {
      String base64Token = token.replace(token.substring(0, 7), "");
      newTransaction =
          accountService.realiseTransaction( customerService.getCustomerFromToken(base64Token),
          transaction.getFromIban(), transaction.getToIban(), transaction.getAmount(), currency);
    } catch (IOException | NoSuchMethodException e) {
      throw new TransactionErrorException(e.getMessage());
    }

    if (newTransaction.getStatus().equals("Realised")) {
      return ResponseEntity.ok().body(newTransaction);
    }
    return ResponseEntity.badRequest().body(newTransaction);
  }

  @RequestMapping(value = "/movements", method = GET)
  public ResponseEntity<TransactionsDto> getCustomerTransactions(
      @RequestHeader(name = "Authorization") String token) {

    String base64Token = token.replace(token.substring(0, 7), "");
    Customer customer = customerService.getCustomerFromToken(base64Token);

    return ResponseEntity.ok().body(new TransactionsDto(customer.getTransactions()));
  }
}
