package com.keypass.server.account;

public class AccountOperationNotPermitedException extends RuntimeException {
    public AccountOperationNotPermitedException(String message){
        super(message);
    }
}
