package com.firm.brokage.service.demo.controllers.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> customValidationErrorHandling(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String message = error.getDefaultMessage();
      body.put("error", message);
    });
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
