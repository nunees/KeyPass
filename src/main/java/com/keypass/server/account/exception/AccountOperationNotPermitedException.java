package com.keypass.server.account.exception;

public class AccountOperationNotPermitedException extends RuntimeException {
    public AccountOperationNotPermitedException(String message){
        super(message);
    }
}
