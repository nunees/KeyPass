package com.keypass.server.auth;

public record AuthenticationRequestDto(
    String username,
    String password
) {
}
