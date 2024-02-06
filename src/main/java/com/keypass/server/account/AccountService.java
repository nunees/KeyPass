package com.keypass.server.account;

import com.keypass.server.exception.AccountControllerException.AccountControllerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account create(Account account) {
        if (accountRepository.findByUsername(account.getUsername()).isPresent()
                || accountRepository.findByEmail(account.getEmail()).isPresent())
            throw new AccountControllerException("User already exists");

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

    public void deleteAccountById(String id) {
        accountRepository.deleteById(UUID.fromString(id));
    }

}
