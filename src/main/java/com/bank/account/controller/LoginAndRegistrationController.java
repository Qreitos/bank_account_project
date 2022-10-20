package com.bank.account.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bank.account.events.OnRegistrationCompleteEvent;
import com.bank.account.exception.InvalidConfirmationTokenException;
import com.bank.account.exception.LoginErrorException;
import com.bank.account.exception.RegistrationErrorException;
import com.bank.account.model.dto.ConfirmationResponseDto;
import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.LoginResponseDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.bank.account.model.dto.RegistrationResponseDto;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.VerificationToken;
import com.bank.account.service.CustomerService;
import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class LoginAndRegistrationController {

  private final CustomerService customerService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @RequestMapping(value = "/login", method = POST)
  public ResponseEntity<LoginResponseDto> loginData(@RequestBody LoginRequestDto customerLogin) {

    String authenticationStatus = customerService.authentication(customerLogin);

    if (authenticationStatus.equals("number=fail")) {
      throw new LoginErrorException("Login number not found");
    }
    if (authenticationStatus.equals("password=fail")) {
      throw new LoginErrorException("Wrong password");
    }
    if (!customerService.getCustomerByLoginNumber(customerLogin.getLoginNumber()).isEnabled()) {
      throw new LoginErrorException("Email not verified");
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
      @RequestBody RegistrationRequestDto registrationData,
      HttpServletRequest request) {

    if (registrationData.getForName() == null
        || registrationData.getSurName() == null
        || registrationData.getPassword() == null
        || registrationData.getBirthDate() == null
        || registrationData.getEmail() == null) {
      throw new RegistrationErrorException("Invalid request");
    }

    Integer newLoginNumber = customerService.createAndSaveNewCustomer(registrationData);

    Customer customer = customerService.getCustomerByLoginNumber(newLoginNumber);
    String url = request.getContextPath();
    this.applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(customer, url));

    return ResponseEntity.accepted().body(
        new RegistrationResponseDto("Registration successful. "
            + "Please confirm verification email and save your login number",
            newLoginNumber, ACCEPTED, ZonedDateTime.now()));
  }

  @RequestMapping(value = "/verification", method = POST)
  public ResponseEntity<ConfirmationResponseDto> emailConfirmation(
      @RequestParam(name = "token") String token) {

    VerificationToken verificationToken = this.customerService.getVerificationToken(token);
    if (verificationToken != null && verificationToken.getCustomer() != null) {
      Customer customer = verificationToken.getCustomer();
      customer.setEnabled(true);
      this.customerService.saveCustomer(customer);
      return ResponseEntity.ok(
          new ConfirmationResponseDto("Verification successful, proceed to login",
              ACCEPTED,
              ZonedDateTime.now()));
    } else {
      throw new InvalidConfirmationTokenException("Verification failed");
    }
  }
}
