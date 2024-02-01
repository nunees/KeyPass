package com.keypass.server.token;

import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.keypass.server.account.Account;
import com.keypass.server.account.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtEncoder encoder;

    @Value("${application.security.jwt.access.token.expiresIn}")
    private Long accessTokenExpiresIn;

    @Value("${application.security.jwt.refresh.token.expiresIn}")
    private Long refreshTokenExpiresIn;

    @Value("${application.security.jwt.issuer}")
    private String tokenIssuer;

    public JwtService(JwtEncoder encoder, RefreshTokenService refreshTokenService, AccountService accountService) {
        this.encoder = encoder;
    }

    public HashMap<String, Object> generateTokens(Authentication authentication) {
        Instant now = Instant.now();

        String scopes = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        var access_claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();

        var refresh_claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpiresIn))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();

        String accessToken = encoder.encode(JwtEncoderParameters.from(access_claims)).getTokenValue();
        String refreshToken = encoder.encode(JwtEncoderParameters.from(refresh_claims)).getTokenValue();

        HashMap<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public HashMap<String, Object> generateTokensWithoutAuth(Account account) {
        Instant now = Instant.now();

        String scopes = "user";

        var access_claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .subject(account.getUsername())
                .claim("scope", scopes)
                .build();

        var refresh_claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpiresIn))
                .subject(account.getUsername())
                .claim("scope", scopes)
                .build();

        String accessToken = encoder.encode(JwtEncoderParameters.from(access_claims)).getTokenValue();
        String refreshToken = encoder.encode(JwtEncoderParameters.from(refresh_claims)).getTokenValue();

        HashMap<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public HashMap<String, Object> generateAcessTokenWithoutAuth(Account account) {
        Instant now = Instant.now();

        String scopes = "user";

        var access_claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .subject(account.getUsername())
                .claim("scope", scopes)
                .build();

        String accessToken = encoder.encode(JwtEncoderParameters.from(access_claims)).getTokenValue();

        HashMap<String, Object> token = new HashMap<>();
        token.put("accessToken", accessToken);

        return token;
    }


}
