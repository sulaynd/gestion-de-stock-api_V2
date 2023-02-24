package com.talixmines.management.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ResourceAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private ErrorCodes errorCode;

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String pattern, Object ... arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    public ResourceAlreadyExistException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ResourceAlreadyExistException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

