package com.keypass.server.account;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keypass.server.exception.AccountControllerException.AccountControllerException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/accounts")
@Tag(name = "Accounts", description = "Create and manage user accounts")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;

  @Operation(summary = "Create a new account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Account created"),
      @ApiResponse(responseCode = "401", description = "User Already Exists"),
      @ApiResponse(responseCode = "500", description = "Bad Request")
  })
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> create(@RequestBody AccountRequestDto accountRequestDto) {
    try {
      String hashedPassword = new BCryptPasswordEncoder().encode(accountRequestDto.password());
      Account newAccount = Account.builder()
              .firstName(accountRequestDto.firstName())
              .lastName(accountRequestDto.lastName())
              .username(accountRequestDto.username())
              .password(hashedPassword)
              .email(accountRequestDto.email())
              .build();

      Account userCreated = accountService.create(newAccount);

      AccountResponseDto response = new AccountResponseDto(
              userCreated.getId(),
              userCreated.getFirstName(),
              userCreated.getLastName(),
              userCreated.getUsername(),
              userCreated.getEmail()
      );

      return ResponseEntity.status(201).body(response);

    } catch (AccountControllerException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "Get account by Id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Account found"),
      @ApiResponse(responseCode = "400", description = "Account not found"),
      @ApiResponse(responseCode = "500", description = "Bad Request")
  })
  @SecurityRequirement(name = "bearer-key")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAccountById(@PathVariable("id") String id) {
    try {
      Account account = accountService.getAccountById(id).orElseThrow(() -> {
        throw new AccountControllerException("Account not found");
      });
      return ResponseEntity.ok(account);
    } catch (AccountControllerException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "Update account by Id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Account updated"),
      @ApiResponse(responseCode = "400", description = "Account not found"),
      @ApiResponse(responseCode = "500", description = "Bad Request")
  })
  @SecurityRequirement(name = "bearer-key")
  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateAccountById(@PathVariable("id") String id,
      @RequestBody AccountRequestDto accountRequestDto) {
    try {
      Account account = accountService.getAccountById(id).orElseThrow(() -> {
        throw new AccountControllerException("Account not found");
      });

      account.setFirstName(accountRequestDto.firstName());
      account.setLastName(accountRequestDto.lastName());
      account.setUsername(accountRequestDto.username());
      account.setEmail(accountRequestDto.email());
      account.setPassword(new BCryptPasswordEncoder().encode(accountRequestDto.password()));

      accountService.create(account);

      return ResponseEntity.ok(account);
    } catch (AccountControllerException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Operation(summary = "Delete account by Id")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Accountdeleted"),
      @ApiResponse(responseCode = "400", description = "Account not found"),
      @ApiResponse(responseCode = "500", description = "Bad Request")

  })
  @SecurityRequirement(name = "bearer-key")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteAccountById(@PathVariable("id") String id) {
    try {
      Account account = accountService.getAccountById(id).orElseThrow(() -> {
        throw new AccountControllerException("Account not found");
      });

      accountService.deleteAccountById(account.getId().toString());

      return ResponseEntity.ok(account);
    } catch (AccountControllerException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
