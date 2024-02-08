package com.keypass.server.account;

import com.keypass.server.account.dto.AccountUpdateRequestDto;
import com.keypass.server.account.exception.AccountAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account create(Account account) {
        if(getAccountByEmail(account.getEmail()) != null){
            throw new AccountAlreadyExistException("Email already in use");
        }

        if(getAccountByUsername(account.getUsername()) != null){
            throw new AccountAlreadyExistException("Username already taken");
        }

        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(UUID.fromString(id));
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    public Account getAccountByEmail(String email) {
       return accountRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public int updateAccount(UUID userId, AccountUpdateRequestDto accountUpdateRequestDto) {

        Account accountStored = getAccountById(userId.toString()).orElse(null);

        if (accountStored == null || accountUpdateRequestDto == null) {
            return 0;
        }

        return accountRepository.updateAccountById(userId, accountUpdateRequestDto);

    }

    public void deleteAccountById(String id) {
        accountRepository.deleteById(UUID.fromString(id));
    }

}
