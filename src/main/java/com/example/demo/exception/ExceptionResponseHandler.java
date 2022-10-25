package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherException(final Exception ex) {
        //LOGGER.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpHeaders.EMPTY, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex,
                                                             final Object body, final HttpHeaders headers,
                                                             final HttpStatus status, final WebRequest request) {
        //LOGGER.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpHeaders.EMPTY, HttpStatus.NOT_ACCEPTABLE);
    }
}
