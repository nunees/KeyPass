package com.keypass.server.auth;


import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final AccountRepository accountRepository;
  private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return accountRepository.findByUsername(username)
      .map(UserAuthenticated::new)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
