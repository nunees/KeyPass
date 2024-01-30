package com.keypass.server.account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>{
  Optional<Account> findByUsername(String username);
  Optional<Account> findByEmail(String email);
  Optional<Account> findByUsernameOrEmail(String username, String email);
  Boolean existsByUsername(String username);
}
