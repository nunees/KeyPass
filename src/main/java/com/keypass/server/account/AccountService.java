package com.keypass.server.account;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  public Account create(Account account) {
    return accountRepository.save(account);
  }

  public Account getAccountById(String id) {
    UUID user_id = UUID.fromString(id);
    return accountRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public Account getAccountByUsername(String username) {
    return accountRepository.findByUsername(username).orElseThrow();
  }

  public Account getAccountByEmail(String email) {
    return accountRepository.findByEmail(email).get();
  }

}
