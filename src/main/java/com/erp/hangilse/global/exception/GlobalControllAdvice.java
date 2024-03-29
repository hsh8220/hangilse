package com.erp.hangilse.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalControllAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> badCredentialsException (BadCredentialsException e) {
        log.error("login fail exception", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.LOGIN_FAILED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> accessDeniedException (AccessDeniedException e) {
        log.error("access denied", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenValidFailedException.class)
    protected ResponseEntity<ErrorResponse> tokenValidFailedException (TokenValidFailedException e) {
        log.error("token valid fail exception", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_GENERATION_FAILED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> noSuchElementException (NoSuchElementException e) {
        log.error("no such element exception", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> insufficientAuthenticationException (InsufficientAuthenticationException e) {
        log.error(e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> internalServerException (Exception e) {
        log.error("internal server exception", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> illegalArgumentException (IllegalArgumentException e) {
        log.error(e.getMessage());
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(ErrorCode.INVALID_INPUT_VALUE.getCode());
        errorResponse.setStatus(ErrorCode.INVALID_INPUT_VALUE.getStatus());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<ErrorResponse> messagingException (MessagingException e) {
        log.error("mail send exception", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
