package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {};
}
