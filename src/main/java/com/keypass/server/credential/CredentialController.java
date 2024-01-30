package com.keypass.server.credential;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

  @Operation(summary = "Create a new credential")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Credential created"),
    @ApiResponse(responseCode = "500", description = "Bad Request")
  })
  @PostMapping("/new")
  @ResponseBody
  public ResponseEntity<Credential> create(@RequestBody Credential credential) {
    return ResponseEntity.ok(credentialService.create(credential));
  }
}
