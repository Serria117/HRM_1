package com.hrm.customExpeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice @Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler
{
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request)
    {
        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList());
        var response = new HashMap<String, Object>();
        response.put("errors", errors);
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({
            DuplicatedEntityException.class,
            IdNotFoundException.class,
            InvalidIdentityException.class})
    public ResponseEntity<?> handleCustomException(Exception ex, WebRequest web)
    {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleSystemException(Exception ex, WebRequest web)
    {
        log.error(ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.badRequest().body("The server has encountered an error when processing your request. A report has been generated for " +
                                                        "the administrator.");
    }
}
