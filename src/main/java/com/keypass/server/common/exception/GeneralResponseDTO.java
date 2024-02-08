package com.keypass.server.common.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record GeneralResponseDTO(
        int statusCode,
        String message,
        ZonedDateTime timestamp
) {
}
