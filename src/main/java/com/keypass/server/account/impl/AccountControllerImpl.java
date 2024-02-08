package com.keypass.server.account.impl;

import com.keypass.server.account.*;
import com.keypass.server.common.exception.GeneralException;
import com.keypass.server.common.exception.GeneralResponseDTO;
import com.keypass.server.common.exception.MissingFieldsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/accounts")
@Tag(name = "Accounts", description = "Create and manage user accounts")
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountServiceImpl accountServiceImpl;

    private final ModelMapper modelMapper;


    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountRequestDTO accountRequestDto) {

        if (accountRequestDto.firstName() == null || accountRequestDto.lastName() == null || accountRequestDto.username() == null || accountRequestDto.email() == null || accountRequestDto.password() == null) {
            throw new MissingFieldsException("Check for blank fields on the form");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(accountRequestDto.password());
        Account newAccount = Account.builder().firstName(accountRequestDto.firstName()).lastName(accountRequestDto.lastName()).username(accountRequestDto.username()).password(hashedPassword).email(accountRequestDto.email()).build();

        Account userCreated = accountServiceImpl.create(newAccount);

        AccountResponseDTO response = new AccountResponseDTO(userCreated.getId(), userCreated.getFirstName(), userCreated.getLastName(), userCreated.getUsername(), userCreated.getEmail());

        return ResponseEntity.status(201).body(response);

    }


    public ResponseEntity<Object> getAccountById(@PathVariable("id") String userId) {

        Account userAccount = accountServiceImpl.getAccountById(userId).orElse(null);

        if (userAccount != null) {
            return ResponseEntity.ok().body(new AccountResponseDTO(userAccount.getId(), userAccount.getFirstName(), userAccount.getLastName(), userAccount.getUsername(), userAccount.getEmail()));
        }

        return ResponseEntity.notFound().build();
    }



    public ResponseEntity<Object> updateUserAccount(@PathVariable("id") UUID userId, @RequestBody AccountUpdateDTO accountUpdateDTO) {

        if (accountUpdateDTO.firstName() == null || accountUpdateDTO.lastName() == null || accountUpdateDTO.username() == null || accountUpdateDTO.email() == null) {
            throw new MissingFieldsException("Check for blank fields on the form");
        }

        Account userAccount = accountServiceImpl.getAccountById(userId.toString()).orElse(null);

        if (userAccount == null || !userAccount.getId().equals(userId)) {
            throw new AccountOperationNotPermitedException("Operation not allowed");
        }

        int rowsAffected = accountServiceImpl.updateAccount(userId, accountUpdateDTO);

        if (rowsAffected == 0) {
            throw new GeneralException("Failed to update account");
        }
        return ResponseEntity.ok().body(new GeneralResponseDTO(200, "Operation succeeded", ZonedDateTime.now(ZoneId.of("Z"))));
    }
}
