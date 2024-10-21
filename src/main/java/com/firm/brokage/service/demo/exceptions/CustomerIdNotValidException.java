package com.firm.brokage.service.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerIdNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomerIdNotValidException() {
        super("The requested team Id should be UUID.");
    }
}
