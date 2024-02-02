package com.keypass.server.exception.AccountControllerException;

public class AccountControllerException extends RuntimeException {
    public AccountControllerException(String message){
        super(message);
    }

    public AccountControllerException(String message, Throwable cause){
        super(message, cause);
    }
}
