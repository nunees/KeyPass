package com.keypass.server.account.dto;

import lombok.Builder;

@Builder
public record AccountUpdateRequestDto(
        String firstName,
        String lastName,
        String username,
        String email
) {
}
