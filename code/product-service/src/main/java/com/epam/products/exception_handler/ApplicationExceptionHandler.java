package com.epam.products.exception_handler;

import com.epam.products.exception.ProductNotCreatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(ProductNotCreatedException.class)
    public ErrorResponse handleProductNotCreatedException(ProductNotCreatedException productNotCreatedException) {
        log.error("Product creation failed: {}", productNotCreatedException.getMessage(), productNotCreatedException);
        return ErrorResponse.builder(productNotCreatedException, HttpStatus.INTERNAL_SERVER_ERROR, productNotCreatedException.getMessage())
                .build();
    }

    // Fallback for any unexpected/unhandled exception so clients never see a raw stack trace.
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnexpectedException(Exception exception) {
        log.error("Unexpected error occurred: {}", exception.getMessage(), exception);
        return ErrorResponse.builder(exception, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage())
                .build();
    }
}
