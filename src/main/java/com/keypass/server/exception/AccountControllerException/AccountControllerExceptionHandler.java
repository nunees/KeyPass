package com.keypass.server.exception.AccountControllerException;

import com.keypass.server.exception.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AccountControllerExceptionHandler {

    @ExceptionHandler(value = {AccountControllerException.class})
    public ResponseEntity<Object> handleAccountException(AccountControllerException a){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(a.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("z")));
        return new ResponseEntity<>(apiException,badRequest);
    }
}
