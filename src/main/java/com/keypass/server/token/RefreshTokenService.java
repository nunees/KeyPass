package com.keypass.server.token;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshTokenByUserId(String userId){
        return refreshTokenRepository.findByAccountId((UUID.fromString(userId))).orElseThrow();
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
