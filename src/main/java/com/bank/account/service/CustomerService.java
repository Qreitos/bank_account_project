package com.bank.account.service;

import com.bank.account.model.dto.CustomerResponseDto;
import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.bank.account.model.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {

  String authentication(LoginRequestDto customerLogin);

  UserDetails loadUserByLoginNumber(int loginNumber);

  String getToken(UserDetails loggingUser);

  Integer createAndSaveNewCustomer(RegistrationRequestDto registrationDto);

  Customer getCustomerFromToken(String token);

  Customer getCustomerByLoginNumber(int loginNumber);

  CustomerResponseDto transferCustomerToDto(Customer customer);
}
