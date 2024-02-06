package com.keypass.server.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshTokenByUserId(String userId) {
        return refreshTokenRepository.findByAccountId((UUID.fromString(userId))).orElseThrow();
    }

    public RefreshToken findById(String id) {
        UUID refreshTokenId = UUID.fromString(id);
        return refreshTokenRepository.findById(refreshTokenId).orElseThrow();
    }

    public RefreshToken getRefreshTokenById(UUID id) {
        return refreshTokenRepository.findById(id).orElseThrow();
    }

    public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiresIn().isBefore(LocalDateTime.now());
    }

    public boolean isRefreshTokenRevoked(RefreshToken refreshToken) {
        return refreshToken.isRevoked();
    }

    public void deleteRefreshToken(UUID refreshTokenId) {
        refreshTokenRepository.deleteById(refreshTokenId);
    }

}
