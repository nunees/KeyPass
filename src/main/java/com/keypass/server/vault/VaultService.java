package com.keypass.server.vault;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VaultService {
  private final VaultRepository vaultRepository;

  public Vault save(Vault vault) {
    return vaultRepository.save(vault);
  }

  public Vault findById(String id) {
    return vaultRepository.findById(UUID.fromString(id)).orElseThrow();
  }

  public Vault findAllUserVaults(UUID accountId) {
    return vaultRepository.findAllByAccountId(accountId).orElseThrow();
  }

  public void deleteVault(UUID vaultId) {
    vaultRepository.deleteById(vaultId);
  }

}
