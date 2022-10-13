package com.bank.account;

import com.bank.account.model.entity.Role;
import com.bank.account.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BankApplication {

  private final RoleRepository roleRepository;

  public static void main(String[] args) {
    SpringApplication.run(BankApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      roleRepository.save(new Role("USER"));
      roleRepository.save(new Role("ADMIN"));
    };
  }

}
