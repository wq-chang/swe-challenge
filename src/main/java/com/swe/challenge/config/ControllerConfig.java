package com.swe.challenge.config;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerConfig {

  @ExceptionHandler({
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    ResourceExistException.class,
    ResourceNotFoundException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleConstraintViolationException(Exception ex) {
    return ex.getMessage();
  }
}
