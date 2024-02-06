package com.keypass.server.account;

import lombok.Builder;

@Builder
public record AccountUpdateRequestDto(
        String firstName,
        String lastName,
        String username,
        String email
) {
}
