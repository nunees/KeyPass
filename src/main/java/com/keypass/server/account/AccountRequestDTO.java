package com.keypass.server.account;

import lombok.Builder;

@Builder
public record AccountRequestDTO(String firstName, String lastName, String username, String email, String password) {

}
