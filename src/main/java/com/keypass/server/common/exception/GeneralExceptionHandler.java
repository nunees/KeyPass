package com.keypass.server.common.exception;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handle(HttpMessageNotReadableException ex){
        if(ex != null){
            GeneralResponseDTO generalResponseDTO =
                    new GeneralResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "There is an error with the request form", ZonedDateTime.now(ZoneId.of("Z")));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generalResponseDTO);
        }
        return ResponseEntity.internalServerError().body("Server not available");
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<Object> handle(InvalidBearerTokenException ex){
        if(ex != null){
            GeneralResponseDTO generalResponseDTO =
                    new GeneralResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid token", ZonedDateTime.now(ZoneId.of("Z")));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generalResponseDTO);
        }
        return ResponseEntity.internalServerError().body("Invalid token");
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<Object> handle(Exception ex){
        if(ex != null){
            GeneralResponseDTO generalResponseDTO =
                    new GeneralResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ZonedDateTime.now(ZoneId.of("Z")));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generalResponseDTO);
        }
        return ResponseEntity.internalServerError().body("Invalid token");
    }

}
