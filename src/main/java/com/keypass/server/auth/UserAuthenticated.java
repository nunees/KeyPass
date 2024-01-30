package com.keypass.server.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.keypass.server.account.Account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthenticated implements UserDetails {
  private final Account account;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "all");
  }

  @Override
  public String getPassword() {
    return account.getPassword();
  }

  @Override
  public String getUsername() {
    return account.getUsername();
  }

  public String getEmail() {
    return account.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return account.isEnabled();
  }

}
