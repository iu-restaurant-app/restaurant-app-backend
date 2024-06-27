package iu.frontenders.restaurantappbackend.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            Exception.class,
            RuntimeException.class
    })
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                ResponseBody.builder()
                        .exceptionName(exception.getClass().getName())
                        .detail(exception.getMessage())
                        .build(),
                new HttpHeaders(),
                HttpStatus.I_AM_A_TEAPOT,
                request
        );
    }
}
