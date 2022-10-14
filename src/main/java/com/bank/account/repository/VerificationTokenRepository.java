package com.bank.account.repository;

import antlr.Token;
import com.bank.account.model.entity.Customer;
import com.bank.account.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  VerificationToken findVerificationTokenByToken(String token);
}
