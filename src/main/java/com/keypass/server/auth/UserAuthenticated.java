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
    // TODO Auto-generated method stub
    return List.of(() -> "all");
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return account.getPassword();
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return account.getUsername();
  }

  public String getEmail() {
    return account.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true;
  }

}
