package com.keypass.server.token;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  private final AccountService accountService;
  @Autowired
  private final JwtService jwtService;

  @Operation(summary = "Create a new refresh token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Refresh token created"),
  })
  @PostMapping("/new")
  @ResponseBody
  public ResponseEntity<Object> create(@RequestBody String refreshToken) {
    RefreshToken storedRefreshToken = refreshTokenService.getRefreshTokenByHash(refreshToken.trim());
    if (storedRefreshToken == null) {
      return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    System.out.println(storedRefreshToken);

    refreshTokenService.deleteRefreshToken(storedRefreshToken.getId());

    Account account = accountService.getAccountById(storedRefreshToken.getAccount().getId().toString());
    if (account == null) {
      return ResponseEntity.badRequest().body("Your refresh token does not belong to you");
    }

    Object tokens = jwtService.generateTokensWithoutAuth(account);

    return ResponseEntity.ok().body(tokens);
  }
}
