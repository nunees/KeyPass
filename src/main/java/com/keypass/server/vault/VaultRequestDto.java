package com.keypass.server.vault;

public record VaultRequestDto(
    String name,
    String description,
    String password,
    String confirmPassword) {
}
