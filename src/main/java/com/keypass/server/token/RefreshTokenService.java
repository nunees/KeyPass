package com.keypass.server.token;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshTokenByHash(String refreshTokenHash) {
        return refreshTokenRepository.findByToken(refreshTokenHash);
    }

    public RefreshToken findById(String id) {
        UUID refreshTokenId = UUID.fromString(id);
        return refreshTokenRepository.findById(refreshTokenId).orElseThrow();
    }

    public RefreshToken getRefreshTokenById(UUID id) {
        return refreshTokenRepository.findById(id).orElseThrow();
    }

    public void deleteRefreshToken(UUID refreshTokenId) {
        refreshTokenRepository.deleteById(refreshTokenId);
    }

}
