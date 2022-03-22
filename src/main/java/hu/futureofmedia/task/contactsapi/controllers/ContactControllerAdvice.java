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
    protected ResponseEntity<Object> handleConstraintViolationException(
            RuntimeException ex, WebRequest request) {

        String bodyOfResponse;

        if (ex.getMessage().contains("must be a well-formed email address"))
        {
            bodyOfResponse = "Helyes formátumú e-mail címnek kell lennie";
        }
        else if(ex.getMessage().contains("Vezetéknév megadása kötelező"))
        {
            bodyOfResponse = "Vezetéknév megadása kötelező";
        }
        else if(ex.getMessage().contains("Vezetéknév nem lehet üres"))
        {
            bodyOfResponse = "Vezetéknév nem lehet üres";
        }
        else if(ex.getMessage().contains("Keresztnév megadása kötelező"))
        {
            bodyOfResponse = "Meg kell adni a keresztnevet";
        }
        else if(ex.getMessage().contains("Keresztnév nem lehet üres"))
        {
            bodyOfResponse = "Keresztnév nem lehet üres";
        }
        else if(ex.getMessage().contains("E-mail cím megadása kötelező"))
        {
            bodyOfResponse = "E-mail cím megadása kötelező";
        }
        else if(ex.getMessage().contains("E-mail cím nem lehet üres"))
        {
            bodyOfResponse = "E-mail cím nem lehet üres";
        }

        else if(ex.getMessage().contains("A cég kiváalsztása kötelező"))
        {
            bodyOfResponse = "A cég kiváalsztása kötelező";
        }
        else if(ex.getMessage().contains("Válasszon a kiválasztható cégek közül"))
        {
            bodyOfResponse = "Válasszon a kiválasztható cégek közül";
        }
        else
        {
            bodyOfResponse = "Eddig még külön nem kezelt hiba: " + ex.getMessage();
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
    protected ResponseEntity<Object> handleStatusInvalidException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { NullPointerException.class, NullPointerException.class })
    protected ResponseEntity<Object> handleStatusNullException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Ki kell választani egy státuszt";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
