package com.keypass.server.auth;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountService;
import com.keypass.server.token.JwtService;
import com.keypass.server.token.RefreshToken;
import com.keypass.server.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final JwtService jwtService;
  private final AccountService accountService;
  private final RefreshTokenService refreshTokenService;

  public Object authenticate(Authentication authentication) {

    HashMap<String, Object> tokens = jwtService.generateToken(authentication);

    Account currentUser = accountService.getAccountByUsername(authentication.getName());

    LocalDateTime expiresIn = LocalDateTime.now().plusSeconds(1296000L);

    RefreshToken savedRefreshToken = RefreshToken.builder()
        .token(tokens.get("refreshToken").toString())
        .account(currentUser)
        .expiresIn(expiresIn)
        .build();

    refreshTokenService.save(savedRefreshToken);

    return tokens;

  }

}
