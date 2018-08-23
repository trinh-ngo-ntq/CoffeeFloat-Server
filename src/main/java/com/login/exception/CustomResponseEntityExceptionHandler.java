package com.login.exception;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.login.util.Constants;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param ex
     * @param request
     * @return
     */

    @ExceptionHandler(value = {InvalidAccessToken.class})
    protected ResponseEntity<Object> handleNotFound(final InvalidAccessToken ex) {
        ex.printStackTrace();
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND.toString(), new Date(),
                Constants.HTTP_STATUS_MSG.ERROR_NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(responseError);
    }

    /**
     * Handle remain exception, return code 500 with empty body
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleInvalidToken(InvalidAccessToken ex) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
