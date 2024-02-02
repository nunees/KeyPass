package com.keypass.server.account;

import com.keypass.server.exception.AccountControllerException.AccountControllerException;
import jdk.jfr.ContentType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.print.attribute.standard.Media;

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
    try{
      String hashedPassword = new BCryptPasswordEncoder().encode(accountRequestDto.password());
      Account newAccount = Account.builder()
        .firstName(accountRequestDto.firstName())
        .lastName(accountRequestDto.lastName())
        .username(accountRequestDto.username())
        .password(hashedPassword)
        .email(accountRequestDto.email())
        .build();
    //return ResponseEntity.ok(accountService.create(newAccount));
      return ResponseEntity.status(201).body(null);
    } catch (AccountControllerException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
