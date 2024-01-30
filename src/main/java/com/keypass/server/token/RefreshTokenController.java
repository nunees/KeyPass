package com.keypass.server.token;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/refresh-token", produces = "application/json")
@Tag(name = "Refresh Token", description = "Create and manage refresh tokens")
@RequiredArgsConstructor
public class RefreshTokenController {

  private final RefreshTokenService refreshTokenService;

  @Operation(summary = "Create a new refresh token")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Refresh token created"),
  })
  @PostMapping("/new")
  @ResponseBody
  public void create() {

  }
}
