package com.Superlee.HR.Backend.Business;

public class UnpermittedOperationException extends RuntimeException {
    public UnpermittedOperationException(String message) {
        super(message);
    }

    public UnpermittedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnpermittedOperationException(Throwable cause) {
        super(cause);
    }

    public UnpermittedOperationException() {
        super();
    }
}
