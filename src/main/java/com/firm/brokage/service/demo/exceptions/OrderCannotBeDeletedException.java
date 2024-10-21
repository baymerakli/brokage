package com.firm.brokage.service.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderCannotBeDeletedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderCannotBeDeletedException() {
        super("The requested order can not be deleted");
    }
}
