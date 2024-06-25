package com.rashad.bank.exception;

import com.rashad.bank.dto.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, Locale locale) {
        String message = messageSource.getMessage("endpoint-not-found", null, locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.RESOURCE_NOT_FOUND, message)).build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomResponse> handleNotFoundException(NotFoundException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomResponse.builder().error(new ErrorResponse(ex.getCode(), message)).build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, Locale locale) {
        String message = messageSource.getMessage("user-not-found", new String[]{ex.getMessage()}, locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.USER_NOT_FOUND, message)).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomResponse> handleBadRequestException(BadRequestException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(ex.getCode(), message)).build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse> handleBadCredentialsException(Locale locale) {
        String message = messageSource.getMessage("bad-credentials", null, locale);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.BAD_CREDENTIALS, message)).build());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CustomResponse.builder().error(new ErrorResponse(ex.getCode(), message)).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongUsernameFormatException.class)
    public ResponseEntity<CustomResponse> handleWrongUsernameFormatException(
            WrongUsernameFormatException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(ex.getCode(), message)).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongPasswordFormatException.class)
    public ResponseEntity<CustomResponse> handleWrongPasswordFormatException(
            WrongPasswordFormatException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(ex.getCode(), message)).build());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomResponse> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, Locale locale) {
        String method = ex.getMessage().substring(ex.getMessage()
                .indexOf("method") + 8, ex.getMessage().indexOf("is") - 2);
        String message = messageSource.getMessage("method-not-allowed", new Object[]{method}, locale);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED, message)).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, Locale locale) {
        String message;
        if (ex.getMessage().contains("Cannot deserialize value of type `java.lang.Long`")) {
            String field = ex.getCause().toString().substring(
                    ex.getCause().toString().indexOf("[\"") + 2, ex.getCause().toString().indexOf("\"]"));
            message = messageSource.getMessage("integer-value", new Object[]{field}, locale);
        } else if (ex.getMessage().contains("Cannot deserialize value of type `java.lang.Double`")) {
            String field = ex.getCause().toString().substring(
                    ex.getCause().toString().indexOf("[\"") + 2, ex.getCause().toString().indexOf("\"]"));
            message = messageSource.getMessage("double-value", new Object[]{field}, locale);
        } else {
            message = messageSource.getMessage("wrong-format", new Object[]{ex.getMessage()}, locale);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.BAD_REQUEST, message)).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> handleInvalidArgument(MethodArgumentNotValidException ex, Locale locale) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errorResponses.add(new ErrorResponse(ErrorCode.REQUIRED_FIELDS,
                        messageSource.getMessage("required-field", new Object[]{error.getField()}, locale))));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(errorResponses).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomResponse> handleNullPointerException(NullPointerException ex, Locale locale) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder()
                .error(new ErrorResponse(ErrorCode.BAD_REQUEST, ex.getMessage())).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponse> handeMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, Locale locale) {
//        String message = messageSource.getMessage("request-param", null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(
                        ErrorCode.REQUEST_PARAM_REQUIRED, ex.getMessage())).build());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CustomResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(CustomResponse.builder().error(new ErrorResponse(ErrorCode.MULTIPART_FILE, ex.getMessage())).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<CustomResponse> handleNumberFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.builder().error(new ErrorResponse(
                        "number", "You have to send number not string")).build());
    }

}
