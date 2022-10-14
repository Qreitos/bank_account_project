package com.bank.account.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.entity.Customer;
import com.bank.account.repository.CustomerRepository;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerServiceImplTest {

  private final CustomerRepository customerRepository;
  private final CustomerService customerService;

  @Autowired
  public CustomerServiceImplTest(CustomerRepository customerRepository,
      CustomerService customerService) {
    this.customerRepository = customerRepository;
    this.customerService = customerService;
  }

//  @Test
//  @Transactional
//  void createAndSaveNewCustomer() {
//    Customer customer = new Customer();
//    customerRepository.save(customer);
//    assertThat(customerRepository.findAll().size()).isOne();
//  }
//
//  @Test
//  @Transactional
//  void authentication() {
//    Customer customer = new Customer();
//    customer.setLoginNumber(1111);
//    customer.setPassword("1111");
//    customerRepository.save(customer);
//
//    LoginRequestDto loginRequestDto = new LoginRequestDto(1111, "0000");
//    Assertions.assertEquals("password=fail",
//        customerService.authentication(loginRequestDto));
//  }
//
//  @Test
//  @Transactional
//  void getCustomerByLoginNumber() {
//    Customer customer = new Customer();
//    customer.setLoginNumber(123456);
//    customerRepository.save(customer);
//    assertThat(customerRepository.findCustomerByLoginNumber(123456)).isNotNull();
//  }
}