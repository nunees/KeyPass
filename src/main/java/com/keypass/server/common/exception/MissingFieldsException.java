package com.keypass.server.common.exception;

public class MissingFieldsException extends RuntimeException{

    public MissingFieldsException(String message){
        super(message);
    }
}
