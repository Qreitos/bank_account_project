package com.bank.account.service;

import com.bank.account.model.dto.CustomerResponseDto;
import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import com.bank.account.model.entity.VerificationToken;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {

  String authentication(LoginRequestDto customerLogin);

  UserDetails loadUserByLoginNumber(int loginNumber);

  String getToken(UserDetails loggingUser);

  Integer createAndSaveNewCustomer(RegistrationRequestDto registrationDto);

  Customer getCustomerFromToken(String token);

  Customer getCustomerByLoginNumber(int loginNumber);

  boolean passwordCorrect(String rawPassword, String encodedPassword);

  CustomerResponseDto transferCustomerToDto(Customer customer);

  Customer getCustomerByVerificationToken(String verificationToken);

  VerificationToken getVerificationToken(String verificationToken);

  VerificationToken createVerificationToken(Customer customer, String token);

  void saveCustomer(Customer customer);

  List<Transaction> getTransactions(Customer customer);
}
