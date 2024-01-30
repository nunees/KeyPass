package com.keypass.server.token;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  public void save(RefreshToken refreshToken) {

  }

}
