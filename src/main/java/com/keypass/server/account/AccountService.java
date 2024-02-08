package com.keypass.server.account;

import com.keypass.server.account.dto.AccountUpdateRequestDto;
import com.keypass.server.account.exception.AccountAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account create(Account account) {
        if(getAccountByEmail(account.getEmail()).getEmail().equals(account.getEmail())){
            throw new AccountAlreadyExistException("Email already in use");
        }

        if(getAccountByUsername(account.getUsername()).getUsername().equals(account.getUsername())){
            throw new AccountAlreadyExistException("Username already taken");
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

    public int updateAccount(UUID userId, AccountUpdateRequestDto accountUpdateRequestDto) {

        Account accountStored = getAccountById(userId.toString()).orElse(null);

        if (accountStored == null) {
            return 0;
        }

        // TODO: implement a better way to do this
        accountStored.setEmail(accountUpdateRequestDto.email());
        accountStored.setUsername(accountUpdateRequestDto.username());
        accountStored.setFirstName(accountUpdateRequestDto.firstName());
        accountStored.setLastName(accountUpdateRequestDto.lastName());

        return accountRepository.updateAccount(userId,
                accountStored.getFirstName(),
                accountStored.getLastName(),
                accountStored.getUsername(),
                accountStored.getEmail()
        );
    }

    public void deleteAccountById(String id) {
        accountRepository.deleteById(UUID.fromString(id));
    }

}
