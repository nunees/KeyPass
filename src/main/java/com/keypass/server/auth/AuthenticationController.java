package com.keypass.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Sessions", description = "Create and manage user sessions")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  private final AuthenticationManager authenticationManager;


  @Operation(summary = "Create a new session")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Session created"),
    @ApiResponse(responseCode = "400", description = "Bad Request"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @PostMapping("/new")
  public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
      Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(
          authenticationRequestDto.username(),
          authenticationRequestDto.password()));
    return ResponseEntity.ok( authenticationService.authenticate(authentication));
  }

}
