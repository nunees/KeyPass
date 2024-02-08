package com.keypass.server.account.dto;

import lombok.Builder;

@Builder
public record AccountRequestDto(
        String firstName,
        String lastName,
        String username,
        String email,
        String password) {

}
