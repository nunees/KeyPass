package com.keypass.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handle(Exception ex){
        if(ex instanceof HttpMessageNotReadableException){
            GeneralResponseDTO generalResponseDTO =
                    new GeneralResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "There is an error with the request form", ZonedDateTime.now(ZoneId.of("Z")));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generalResponseDTO);
        }
        return ResponseEntity.internalServerError().body("Server not available");
    }
}
