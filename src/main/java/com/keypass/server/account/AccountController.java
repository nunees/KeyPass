package com.keypass.server.account;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/accounts", produces = "application/json")
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
  @PostMapping("/register")
  public ResponseEntity<Object> create(@RequestBody AccountRequestDto accountRequestDto) {
    try{

      String hashedPassword = new BCryptPasswordEncoder().encode(accountRequestDto.password()).toString();
      Account newAccount = Account.builder()
        .firstName(accountRequestDto.firstName())
        .lastName(accountRequestDto.lastName())
        .username(accountRequestDto.username())
        .password(hashedPassword)
        .email(accountRequestDto.email())
        .build();
    return ResponseEntity.ok(accountService.create(newAccount));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
