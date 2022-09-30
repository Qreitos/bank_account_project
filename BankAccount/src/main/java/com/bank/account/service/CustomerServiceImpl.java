package com.bank.account.service;

import static com.bank.account.security.SecurityConfig.TOKEN_EXPIRATION_TIME;

import com.bank.account.model.dto.LoginDto;
import com.bank.account.model.dto.RegistrationDto;
import com.bank.account.model.entity.Account;
import com.bank.account.model.entity.Customer;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import io.github.cdimascio.dotenv.Dotenv;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final AccountRepository accountRepository;

  @Override
  public void createAndSaveNewCustomer(RegistrationDto registrationDto) {

    Random random = new Random();
    int newLoginNumber;

    do {
      newLoginNumber = random.nextInt(100000) + 10001;
    }
    while (customerRepository.findCustomerByLoginNumber(newLoginNumber) != null);

    Customer newCustomer = new Customer();
    newCustomer.setLoginNumber(newLoginNumber);
    newCustomer.setForName(registrationDto.getForName());
    newCustomer.setSurName(registrationDto.getSurName());
    newCustomer.setPassword(registrationDto.getPassword());
    newCustomer.setBirthDate(registrationDto.getBirthDate());

    Account newAccount = new Account(newCustomer);

    accountRepository.save(newAccount);
    customerRepository.save(newCustomer);
  }

  @Override
  public String authentication(LoginDto customerLogin) {

    if (customerRepository.findCustomerByLoginNumber(customerLogin.getLoginNumber()) == null) {
      log.info("Login failed: loginNumber");
      return "number=fail";
    }
    if (!customerRepository.findCustomerByLoginNumber(customerLogin.getLoginNumber()).getPassword()
        .equals(customerLogin.getPassword())) {
      log.info("Login failed: password");
      return "password=fail";
    }
    return "ok";
  }

  @Override
  public UserDetails getUserByLoginNumber(int loginNumber) {

    Customer customer = customerRepository.findCustomerByLoginNumber(loginNumber);
    String logNumber = String.valueOf(loginNumber);

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    customer.getRoles().forEach(role -> authorities.add(
        new SimpleGrantedAuthority(role.getName())));

    return new org.springframework.security.core.userdetails.User(
        logNumber, customer.getPassword(), authorities);
  }

  @Override
  public String getToken(UserDetails loggingUser) {

    Dotenv dotenv = Dotenv.load();

    Algorithm algorithm = // setting up new algorithm for signing token
        Algorithm.HMAC256(
            Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"))
                .getBytes(StandardCharsets.UTF_8)); // loading JWT_SECRET_KEY from .env

    return JWT.create() // creating new JWT
        .withSubject(loggingUser.getUsername()) // saving username into JWT
        .withExpiresAt(
            new Date(
                System.currentTimeMillis()
                    + TOKEN_EXPIRATION_TIME)) // saving expiration time into JWT
        .withIssuer(
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/loggingUser/login")
                .toString()) // saving url path into JWT
        .withClaim(
            "roles",
            loggingUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())) // saving roles of user into JWT
        .sign(algorithm); // signing JWT with algorithm from JWT_SECRET_KEY
  }
}
