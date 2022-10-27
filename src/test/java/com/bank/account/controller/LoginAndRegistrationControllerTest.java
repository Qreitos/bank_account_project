package com.bank.account.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bank.account.model.dto.LoginRequestDto;
import com.bank.account.model.dto.RegistrationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LoginAndRegistrationControllerTest {

  @Autowired
  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void can_get_not_found_status() throws Exception {
    LoginRequestDto loginRequestDto = new LoginRequestDto(123, "123");
    mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequestDto)))
        .andExpect(status()
            .isNotFound())
        .andDo(print());
  }

  @Test
  void can_register_data_test() throws Exception {
    RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto();
    registrationRequestDto.setBirthDate("28.01.1994");
    registrationRequestDto.setEmail("green@fox.sk");
    registrationRequestDto.setSurName("Green");
    registrationRequestDto.setForName("Fox");
    registrationRequestDto.setPassword("123");

    mockMvc.perform(post("/api/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registrationRequestDto)))
        .andExpect(status()
            .isAccepted())
        .andDo(print());
  }

  @Test
  void emailConfirmation() throws Exception {
    mockMvc.perform(post("/api/verification")
        .param("token", "testToken"))
        .andExpect(status()
            .isNotAcceptable())
        .andDo(print());
  }
}