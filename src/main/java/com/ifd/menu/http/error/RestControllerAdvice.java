package com.ifd.menu.http.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFound(final HttpServletResponse response, final Throwable ex) {
        log.warn(ex.getMessage(), ex);
        response.setStatus(404);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handlethrowable(final Throwable ex) {
        log.error(ex.getMessage(), ex);
        final ErrorResponse error = new ErrorResponse();
        error.setMsg("Unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}