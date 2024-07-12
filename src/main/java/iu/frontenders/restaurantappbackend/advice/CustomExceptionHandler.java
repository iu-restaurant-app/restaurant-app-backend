package iu.frontenders.restaurantappbackend.advice;

import io.minio.errors.MinioException;
import iu.frontenders.restaurantappbackend.exception.BaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            BaseException.class,
            MinioException.class
    })
    protected ResponseEntity<Object> handleCustomConflict(Exception exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                ResponseBody.builder()
                        .exceptionName(exception.getClass().getName())
                        .detail(exception.getMessage())
                        .stackTrace(Arrays.toString(exception.getStackTrace()))
                        .build(),
                new HttpHeaders(),
                HttpStatus.I_AM_A_TEAPOT,
                request
        );
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                ResponseBody.builder()
                        .exceptionName(exception.getClass().getName())
                        .detail(exception.getMessage())
                        .stackTrace(Arrays.toString(exception.getStackTrace()))
                        .build(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }
}
