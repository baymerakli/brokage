package com.firm.brokage.service.demo.controllers.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> customValidationErrorHandling(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    List<Map<String, String>> errors = new ArrayList<>();

    // Extract field errors
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      Map<String, String> errorDetails = new HashMap<>();
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errorDetails.put("field", fieldName);
      errorDetails.put("error", message);
      errors.add(errorDetails);
    });

    body.put("errors", errors);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
