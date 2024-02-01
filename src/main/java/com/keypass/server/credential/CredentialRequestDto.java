package com.keypass.server.credential;

public record CredentialRequestDto(
        String provider,
        String username,
        String email,
        String password

) {
}
