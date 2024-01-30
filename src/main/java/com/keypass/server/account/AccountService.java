package com.keypass.server.account;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  public Account create(Account account) {
    if(account == null) {
      throw new IllegalArgumentException("Account cannot be null");
    }
    return accountRepository.save(account);
  }

  public Account getAccountById(String id) {
    if(id.isEmpty()) {
      throw new IllegalArgumentException("Id cannot be empty");
    }
    UUID user_id = UUID.fromString(id);
    return accountRepository.findById(user_id).orElseThrow();
  }

  public Account getAccountByUsername(String username) {
    if(username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    return accountRepository.findByUsername(username).orElseThrow();
  }

}
