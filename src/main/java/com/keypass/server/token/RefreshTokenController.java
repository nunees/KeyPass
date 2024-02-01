package com.keypass.server.token;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/refresh-token", produces = "application/json")
@Tag(name = "Refresh Token", description = "Create and manage refresh tokens")
@RequiredArgsConstructor
public class RefreshTokenController {
  private final RefreshTokenService refreshTokenService;
  private final AccountService accountService;
  private final JwtService jwtService;

  @Operation(summary = "Create a new refresh token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Refresh token created"),
  })
  @PostMapping("/new/{userId}")
  public ResponseEntity<Object> create(@PathVariable("userId") String userId, @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
    Account account = accountService.getAccountById(userId);
    if (account == null) {
      return ResponseEntity.badRequest().body("This refresh token does not belong to you");
    }

    RefreshToken storedRefreshToken = refreshTokenService.findRefreshTokenByUserId(account.getId().toString());
    if (storedRefreshToken == null) {
      return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    if(refreshTokenRequestDto.refreshToken().length() != storedRefreshToken.getToken().length()){
      return ResponseEntity.badRequest().body("Size mismatch");
    }

    refreshTokenService.deleteRefreshToken(storedRefreshToken.getId());

    HashMap<String, Object> keys =  jwtService.generateTokensWithoutAuth(account);

    RefreshToken databaseTokenGenerated = RefreshToken.builder()
            .token(keys.get("refreshToken").toString())
            .account(account)
            .expiresIn(LocalDateTime.now().plusSeconds(1296000L))
            .build();

    System.out.println(databaseTokenGenerated);

    refreshTokenService.save(databaseTokenGenerated);

    return ResponseEntity.ok().body(keys);
  }
}
