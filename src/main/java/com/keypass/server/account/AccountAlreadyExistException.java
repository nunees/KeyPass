package com.keypass.server.account;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message){
        super(message);
    }
}
