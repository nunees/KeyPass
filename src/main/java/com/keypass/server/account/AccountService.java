package com.keypass.server.account;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  public Account create(@NotNull Account account) {
    if(accountRepository.findByEmail(account.getEmail()).isPresent()
            || accountRepository.findByUsername(account.getUsername()).isPresent()){
      return null;
    }
    return accountRepository.save(account);
  }

  public Optional<Account> getAccountById(String id) {
    return accountRepository.findById(UUID.fromString(id));
  }

  public Account getAccountByUsername(String username) {
    return accountRepository.findByUsername(username).orElseThrow();
  }

  public Account getAccountByEmail(String email) {
    return accountRepository.findByEmail(email).orElseThrow();
  }

}
