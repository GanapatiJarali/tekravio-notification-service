package com.tekravio.notification.service.exception;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.common.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HandleGlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException: ", ex);
        String fieldError = ex.getBindingResult().getFieldError().getField();
        String fieldMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        Result response = new Result();
        response.setSuccessCode(400);
        response.setSuccessDescription(fieldError + " " + fieldMessage);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(response);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse> validationException(ValidationException ex) {
        log.error("validation exception: {} ", ex.toString());
        Result response = new Result();
        response.setSuccessCode(ex.getErrorCode());
        response.setSuccessDescription(ex.getErrorDescription());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(response);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException exception: {} ", ex.toString());
        Result response = new Result();
        response.setSuccessCode(100);
        log.error("ConstraintViolationException exception: {} ", ex.getMessage());
        String messages[] = ex.getMessage().split("\\.");
        String fieldError = messages[1];
        response.setSuccessDescription(fieldError);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(response);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }


}
