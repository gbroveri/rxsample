package com.ifd.menu.http.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final Throwable ex) {
        log.warn(ex.getMessage(), ex);
        final ErrorResponse error = new ErrorResponse();
        error.setMsg("not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handlethrowable(final Throwable ex) {
        log.error(ex.getMessage(), ex);
        final ErrorResponse error = new ErrorResponse();
        error.setMsg("Unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}