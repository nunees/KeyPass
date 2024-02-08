package com.keypass.server.exception;

public class MissingFieldsException extends RuntimeException{

    public MissingFieldsException(String message){
        super(message);
    }
}
