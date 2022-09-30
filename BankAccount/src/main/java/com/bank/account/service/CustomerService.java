package com.bank.account.service;

import com.bank.account.model.dto.LoginDto;
import com.bank.account.model.dto.RegistrationDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {

  String authentication(LoginDto customerLogin);
  UserDetails getUserByLoginNumber(int loginNumber);
  String getToken(UserDetails loggingUser);
  void createAndSaveNewCustomer(RegistrationDto registrationDto);
}
