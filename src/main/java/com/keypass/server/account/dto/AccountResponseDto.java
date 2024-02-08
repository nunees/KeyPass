package com.keypass.server.account.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountResponseDto(UUID id, String firstName, String lastName, String username, String email) {
}
