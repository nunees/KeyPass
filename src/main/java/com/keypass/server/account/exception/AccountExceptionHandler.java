package com.keypass.server.account.exception;

import com.keypass.server.exception.GeneralResponseDTO;
import com.keypass.server.exception.MissingFieldsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(value = {AccountAlreadyExistException.class})
    public ResponseEntity<Object> handleAccountAlreadyExistException(AccountAlreadyExistException a){
        GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO(HttpStatus.UNAUTHORIZED.value(), a.getMessage(), ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(generalResponseDTO);
    }

    @ExceptionHandler(value = {MissingFieldsException.class})
    public ResponseEntity<Object> handleMissingFieldsException(MissingFieldsException m){
        GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO(HttpStatus.BAD_REQUEST.value(),m.getMessage(),ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponseDTO);
    }
}
