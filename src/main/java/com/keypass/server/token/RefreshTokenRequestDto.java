package com.keypass.server.token;

public record RefreshTokenRequestDto(
        String refreshToken
) {
}
