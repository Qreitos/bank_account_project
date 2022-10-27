package com.bank.account.service;

import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.VerificationToken;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerServiceImplTest {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private VerificationTokenRepository tokenRepository;

  @Test
  void can_create_and_save_newCustomer_test() {
    customerRepository.deleteAll();
    accountRepository.deleteAll();
    RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto();
    registrationRequestDto.setBirthDate("28.01.1994");
    registrationRequestDto.setEmail("green@fox.sk");
    registrationRequestDto.setSurName("Green");
    registrationRequestDto.setForName("Fox");
    registrationRequestDto.setPassword("123");

    customerService.createAndSaveNewCustomer(registrationRequestDto);

    Assertions.assertEquals(1, customerRepository.findAll().size());
    Assertions.assertEquals(1, accountRepository.findAll().size());
  }

  @Test
  void can_fail_authentication_with_loginNumber_test() {
    LoginRequestDto loginRequestDto = new LoginRequestDto(123, "123");

    Assertions.assertEquals("number=fail", customerService.authentication(loginRequestDto));
  }

  @Test
  void can_fail_authentication_with_password_test() {
    Customer customer = new Customer();
    customer.setLoginNumber(123);
    customer.setPassword("test1");
    customerRepository.save(customer);
    LoginRequestDto loginRequestDto = new LoginRequestDto(123, "test2");
    Assertions.assertEquals("password=fail", customerService.authentication(loginRequestDto));
  }

  @Test
  void can_get_customer_by_loginNumber_test() {
    customerRepository.deleteAll();
    Customer customer = new Customer();
    customer.setForName("test");
    customer.setLoginNumber(1234);
    customerRepository.save(customer);

    Assertions.assertEquals("test",
        customerService.getCustomerByLoginNumber(1234).getForName());
  }

  @Test
  void can_create_verification_token_test() {
    tokenRepository.deleteAll();
    Customer customer = new Customer();
    customer.setBirthDate("28.01.1994");
    customer.setEmail("green@fox.sk");
    customer.setSurName("Green");
    customer.setForName("Fox");
    customer.setPassword("123");
    customerRepository.save(customer);

    customerService.createVerificationToken(customer, "testToken");
    Assertions.assertEquals(1, tokenRepository.findAll().size());
  }

  @Test
  void can_get_customer_by_verification_token_test() {
    tokenRepository.deleteAll();
    Customer customer = new Customer();
    customer.setBirthDate("28.01.1994");
    customer.setEmail("green@fox.sk");
    customer.setSurName("Green");
    customer.setForName("Fox");
    customer.setPassword("123");
    customerRepository.save(customer);

    VerificationToken token = customerService.createVerificationToken(customer, "testToken");
    Assertions.assertEquals(customer.getSurName(),
        customerService.getCustomerByVerificationToken(token.getToken()).getSurName());
  }

  @Test
  void can_save_customer_test() {
    customerRepository.deleteAll();
    Customer customer = new Customer();
    customerService.saveCustomer(customer);

    Assertions.assertEquals(1, customerRepository.findAll().size());
  }
}