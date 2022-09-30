package com.bank.account.controller;

import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.RegistrationErrorException;
import com.bank.account.model.dto.LoginDto;
import com.bank.account.model.dto.RegistrationDto;
import com.bank.account.model.dto.ResponseDto;
import com.bank.account.service.CustomerService;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class LoginAndRegistrationController {

  private final CustomerService customerService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<ResponseDto> postLogin(@RequestBody LoginDto customerLogin) {

    String authenticationStatus = customerService.authentication(customerLogin);

    if (authenticationStatus.equals("number=fail")) {
      throw new LoginErrorException("Login number not found");
    }
    if (authenticationStatus.equals("password=fail")) {
      throw new LoginErrorException("Wrong password");
    }

    org.springframework.security.core.userdetails.User loggingUser =
        (org.springframework.security.core.userdetails.User)
            customerService.getUserByLoginNumber(customerLogin.getLoginNumber());

    return ResponseEntity.ok()
        .body(new ResponseDto("Ok", HttpStatus.OK,
            ZonedDateTime.now(), customerService.getToken(loggingUser)));
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<ResponseDto> postRegister(@RequestBody RegistrationDto registrationData) {

    if (registrationData.getForName() == null
    || registrationData.getSurName() == null
    || registrationData.getPassword() == null
    || registrationData.getBirthDate() == null) {
      throw new RegistrationErrorException("Registration failed");
    }

  }
}
