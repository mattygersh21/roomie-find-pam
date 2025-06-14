package emsquare.roomie_find.pam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import emsquare.roomie_find.pam.model.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailUsedException.class)
    public ResponseEntity<ApiError> handleEmailUsedException(EmailUsedException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ApiError> handlePasswordIncorrectException(PasswordIncorrectException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiError> handleEmailNotFoundException(EmailNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }
}