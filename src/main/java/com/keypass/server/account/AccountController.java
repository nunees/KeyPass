package com.keypass.server.account;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;

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
