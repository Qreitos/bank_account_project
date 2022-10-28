package com.bank.account;

import com.bank.account.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankApplication.class, args);
  }
}
