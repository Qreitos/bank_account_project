package com.bank.account.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.RegistrationErrorException;
import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.LoginResponseDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.bank.account.model.dto.RegistrationResponseDto;
import com.bank.account.service.CustomerService;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class LoginAndRegistrationController {

  private final CustomerService customerService;

  @RequestMapping(value = "/login", method = POST)
  public ResponseEntity<LoginResponseDto> loginData(@RequestBody LoginRequestDto customerLogin) {

    String authenticationStatus = customerService.authentication(customerLogin);

    if (authenticationStatus.equals("number=fail")) {
      throw new LoginErrorException("Login number not found");
    }
    if (authenticationStatus.equals("password=fail")) {
      throw new LoginErrorException("Wrong password");
    }

    org.springframework.security.core.userdetails.User loggingUser =
        (org.springframework.security.core.userdetails.User)
            customerService.loadUserByLoginNumber(customerLogin.getLoginNumber());

    return ResponseEntity.ok()
        .body(new LoginResponseDto("Login successful", HttpStatus.OK,
            ZonedDateTime.now(), customerService.getToken(loggingUser)));
  }

  @RequestMapping(value = "/register", method = POST)
  public ResponseEntity<RegistrationResponseDto> registerData(
      @RequestBody RegistrationRequestDto registrationData) {

    if (registrationData.getForName() == null
        || registrationData.getSurName() == null
        || registrationData.getPassword() == null
        || registrationData.getBirthDate() == null) {
      throw new RegistrationErrorException("Invalid request");
    }

    Integer newLoginNumber = customerService.createAndSaveNewCustomer(registrationData);

    return ResponseEntity.accepted().body(
        new RegistrationResponseDto("Registration successful. Please save your login number!",
            newLoginNumber, ACCEPTED, ZonedDateTime.now()));
  }
}
