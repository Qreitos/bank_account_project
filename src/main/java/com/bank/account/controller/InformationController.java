package com.bank.account.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class InformationController {

  @RequestMapping(value = "/information", method = GET)
  public String getApiInformation() {
    return "information";
  }
}
