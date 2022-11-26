package com.bank.account.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.Transaction;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    customerRepository.deleteAll();
  }

  @Test
  void getHomeWithCurrency() throws Exception {
    mockMvc.perform(get("/api/home"))
        .andExpect(status()
            .isBadRequest())
        .andDo(print());
  }

  @Test
  void createAccount() throws Exception {
    Customer customer = new Customer();
    customer.setForName("Green");
    customer.setSurName("Fox");
    customer.setPassword("123");
    customer.setEmail("green@fox.com");
    customer.setLoginNumber(1234);
    customerService.saveCustomer(customer);

    String bearerToken =
        "Bearer " + customerService.getToken(customerService.loadUserByLoginNumber(1234));

    mockMvc.perform(put("/api/create/account")
            .header("Authorization", bearerToken)
            .param("accountType", "INVESTOR"))
        .andExpect(status()
            .isOk())
        .andDo(print());
  }

  @Test
  void realiseTransaction() throws Exception {
    Customer customer = new Customer();
    customer.setForName("Green");
    customer.setSurName("Fox");
    customer.setPassword("123");
    customer.setEmail("green@fox.com");
    customer.setLoginNumber(1234);
    customerService.saveCustomer(customer);

    String bearerToken =
        "Bearer " + customerService.getToken(customerService.loadUserByLoginNumber(1234));

    Transaction transaction = new Transaction();
    transaction.setFromIban("SK45 0120 0000 0031 8920 6398");
    transaction.setToIban("SK45 0120 0000 0050 8578 8292");
    transaction.setAmount(BigDecimal.valueOf(100));

    mockMvc.perform(post("/api/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transaction))
            .header("Authorization", bearerToken)
            .param("currency", "eur"))
        .andExpect(status()
            .isBadRequest());
  }

  @Test
  void getCustomerTransactions() throws Exception {
    Customer customer = new Customer();
    customer.setForName("Green");
    customer.setSurName("Fox");
    customer.setPassword("123");
    customer.setEmail("green@fox.com");
    customer.setLoginNumber(1234);
    customerService.saveCustomer(customer);

    String bearerToken =
        "Bearer " + customerService.getToken(customerService.loadUserByLoginNumber(1234));

    mockMvc.perform(get("/api/movements")
            .header("Authorization", bearerToken))
        .andExpect(status()
            .isOk())
        .andDo(print());
  }
}