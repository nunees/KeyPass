package com.keypass.server.account.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message){
        super(message);
    }
}
