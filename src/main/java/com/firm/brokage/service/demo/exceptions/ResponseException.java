package com.firm.brokage.service.demo.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResponseException extends Exception{
  private Integer httpStatusCode;
  public ResponseException(Integer httpStatusCode) {
    super();
    this.httpStatusCode = httpStatusCode;
  }

  public Integer getHttpStatusCode() {
    return httpStatusCode;
  }

}
