package com.keypass.server.account;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    public Account create(Account account);

    public Optional<Account> getAccountById(String id);

    public Account getAccountByEmail(String email);

    public int updateAccount(UUID userId, AccountUpdateDTO accountUpdateDTO);

}


