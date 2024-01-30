package com.keypass.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.keypass.server.token.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
  private final JwtService jwtService;

  public String authenticate(Authentication authentication) {
    return jwtService.generateToken(authentication);
  }

}
