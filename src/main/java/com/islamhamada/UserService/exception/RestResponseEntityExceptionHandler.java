package com.islamhamada.UserService.exception;

import com.islamhamada.petshop.contracts.model.RestExceptionResponse;
import org.springframework.http.HttpHeaders;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<RestExceptionResponse> handleUserServiceException(UserServiceException exception) {
        log.error(exception);
        return new ResponseEntity<>(RestExceptionResponse.builder()
                .error_code(exception.getError_code())
                .error_message(exception.getMessage())
                .build(), exception.getHttpStatus());
    }

    @Override
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object>handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(RestExceptionResponse.builder()
                .error_code(exception.getDetailMessageCode())
                .error_message(exception.getMessage())
                .build(), status);
    }
}
