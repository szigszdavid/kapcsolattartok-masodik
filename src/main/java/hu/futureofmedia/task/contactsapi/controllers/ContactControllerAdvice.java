package hu.futureofmedia.task.contactsapi.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ContactControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class, ConstraintViolationException.class })
    protected ResponseEntity<Object> handleEmailConflictException(
            RuntimeException ex, WebRequest request) {

        String bodyOfResponse;

        if (ex.getMessage().contains("company"))
        {
            bodyOfResponse = "Nem létező cég lett kiválasztva";
        }
        else if (ex.getMessage().contains("email"))
        {
            bodyOfResponse = "Az e-mail címnek helyes formátumúnak kell lennie!";
        }
        else
        {
            bodyOfResponse = "Eddig még külön nem kezelt hiba" + ex.getMessage();
        }

        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { NumberFormatException.class, NumberFormatException.class })
    protected ResponseEntity<Object> handlePhoneNumberException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "A telefonszámnak helyes formátumúnak kell lennie";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalArgumentException.class })
    protected ResponseEntity<Object> handleStatusException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}