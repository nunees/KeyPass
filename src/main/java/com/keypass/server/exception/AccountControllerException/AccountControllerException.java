package com.keypass.server.exception.AccountControllerException;


public class AccountControllerException extends RuntimeException {
    /** Constructs a new Account controller exception with the specified detail message.
     * The cause is not initialized.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     */
    public AccountControllerException(String message){
        super(message);
    }

    /** Constructs a new Account controller exception with the specified detail message and
     *  a cause. <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     *  this runtime exception's detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     * @param cause the cause (which is saved for later retrieval by the
     *          {@link #getCause()} method).  (A {@code null} value is
     *          permitted, and indicates that the cause is nonexistent or
     *           unknown.)
     */
    public AccountControllerException(String message, Throwable cause){
        super(message, cause);
    }
}
