package com.login.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.login.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    // 400
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_BAD_REQUEST, ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NumberFormatException.class, JsonSyntaxException.class})
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_BAD_REQUEST, ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_BAD_REQUEST, ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.toString(), new Date(),
                "Validation Failed", ex.getBindingResult().toString());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ResponseError responseError = new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.toString(), new Date(),
                "Validation Failed", error);
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(final BusinessException ex, final WebRequest request) {
        ResponseError responseError = new ResponseError(Constants.HTTP_STATUS.UNPROCESSABLE_ENTITY_EXT, new Date(),
                "Validation Failed", ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // 404
    @ExceptionHandler(value = {EntityNotFoundException.class, ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        ResponseError responseError = new ResponseError(HttpStatus.CONFLICT.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_BAD_REQUEST, ex.getMessage());
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 500
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class,
            SQLException.class, ParseException.class, JsonParseException.class, IOException.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error(HttpStatus.INTERNAL_SERVER_ERROR + " " + HttpStatus.class.getName(), ex);
        ResponseError responseError = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_COMMON, ex.getClass().getName() + " " + ex.getMessage());
        return handleExceptionInternal(ex, responseError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
