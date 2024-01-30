package com.keypass.server.account;

public record AccountRequestDto(
  String firstName,
  String lastName,
  String username,
  String email,
  String password
) {

}
