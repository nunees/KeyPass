package com.keypass.server.account;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountResponseDTO(UUID id, String firstName, String lastName, String username, String email) {
}
