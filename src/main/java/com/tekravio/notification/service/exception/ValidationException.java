package com.tekravio.notification.service.exception;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException{
    private int errorCode;
    private String errorDescription;

    public ValidationException(int errorCode,String message,  String errorDescription) {
        super(message);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
