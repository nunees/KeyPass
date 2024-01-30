package com.keypass.server.credential;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialService {
  private final CredentialRepository credentialRepository;

  public Credential create(Credential credential) {
    if(credential == null) {
      throw new IllegalArgumentException("Credential cannot be null");
    }
    return credentialRepository.save(credential);
  }
}
