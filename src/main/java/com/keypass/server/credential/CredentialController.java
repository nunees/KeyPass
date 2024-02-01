package com.keypass.server.credential;

import com.keypass.server.vault.Vault;
import com.keypass.server.vault.VaultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/credentials", produces = "application/json")
@Tag(name = "Credentials", description = "Create and manage user credentials")
@RequiredArgsConstructor
public class CredentialController {

  private final CredentialService credentialService;
  private final VaultService vaultService;

  @Operation(summary = "Create a new credential")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Credential created"),
      @ApiResponse(responseCode = "500", description = "Bad Request")
  })
  @PostMapping("/new/{vaultId}")
  @ResponseBody
  public ResponseEntity<Object> create(
          @RequestBody CredentialRequestDto credentialRequestDto,
          @PathVariable("vaultId") String vaultId) {
    Vault vault = vaultService.findById(vaultId);
    if(vault == null){
      return ResponseEntity.status(400).body("There was an error retrieving user Vault");
    }

    Credential newCredential = Credential.builder()
            .email(credentialRequestDto.email())
            .username(credentialRequestDto.username())
            .provider(credentialRequestDto.provider())
            .password(credentialRequestDto.password())
            .vault(vault)
            .build();

    credentialService.create(newCredential);

    return ResponseEntity.ok(credentialService.create(newCredential));
  }

  @GetMapping("/hello")
  public String GetHello() {
    return "new SomeData()";
  }

}
