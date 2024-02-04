package com.keypass.server.account;

import java.util.UUID;

public record AccountResponseDto(UUID id, String firstName, String lastName, String username, String email) {
}
