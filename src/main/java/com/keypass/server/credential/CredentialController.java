package com.keypass.server.credential;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/credentials")
@RequiredArgsConstructor
public class CredentialController {

  private final CredentialService credentialService;

  @PostMapping("/new")
  @ResponseBody
  public ResponseEntity<Credential> create(@RequestBody Credential credential) {
    return ResponseEntity.ok(credentialService.create(credential));
  }
}
