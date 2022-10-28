package com.bank.account.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.bank.account.model.entity.Customer;
import com.bank.account.service.CustomerService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomAuthorizationFilterTest {
  @Autowired
  CustomerService customerService;

  @Test
  public void custom_filter_test() throws ServletException, IOException {
    Customer customer = new Customer();
    customer.setForName("Green");
    customer.setSurName("Fox");
    customer.setPassword("123");
    customer.setEmail("green@fox.com");
    customer.setLoginNumber(1234);
    customerService.saveCustomer(customer);

    //Generate valid token
    String bearerToken =
        "Bearer " + customerService.getToken(customerService.loadUserByLoginNumber(1234));
    //For providing valid parameters for authorization filter mocking classes
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    //Creating new filter instance
    CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter();
    //Check if filter throws exception when authorization header is not provided
    Assertions.assertThrows(
        Exception.class,
        () -> customAuthorizationFilter.doFilter(servletRequest, servletResponse, filterChain));
    //Defining how filter finds token from header
    when(servletRequest.getHeader(AUTHORIZATION)).thenReturn(bearerToken);
    customAuthorizationFilter.doFilter(servletRequest, servletResponse, filterChain);
    verify(filterChain).doFilter(servletRequest, servletResponse);
  }
}
