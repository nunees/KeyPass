package com.keypass.server.token;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class old_JWTSERVICE {
    private final JwtEncoder encoder;

    @Value("${application.security.jwt.access.token.expiresIn}")
    private Long accessTokenExpiresIn;

    @Value("${application.security.jwt.refresh.token.expiresIn}")
    private Long refreshTokenExpiresIn;

    @Value("${application.security.jwt.issuer}")
    private String tokenIssuer;

    public old_JWTSERVICE(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication){
        Instant now = Instant.now();

        String scopes = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
