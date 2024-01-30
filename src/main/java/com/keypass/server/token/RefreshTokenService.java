package com.keypass.server.token;

import com.keypass.server.account.Account;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken save(RefreshToken refreshToken) {

      return refreshTokenRepository.save(refreshToken);

  }

}
