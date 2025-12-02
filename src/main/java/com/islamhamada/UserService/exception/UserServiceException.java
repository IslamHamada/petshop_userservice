package com.islamhamada.UserService.exception;

import com.islamhamada.petshop.contracts.exception.ServiceException;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserServiceException extends ServiceException {
    private HttpStatus httpStatus;

    public UserServiceException(String message, String error_code, HttpStatus httpStatus) {
        super(message, "USER_" + error_code);
        this.httpStatus = httpStatus;
    }
}
