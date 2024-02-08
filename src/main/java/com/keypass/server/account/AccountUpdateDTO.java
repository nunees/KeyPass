package com.keypass.server.account;

import lombok.Builder;

@Builder
public record AccountUpdateDTO(String firstName, String lastName, String username, String email) {
}
