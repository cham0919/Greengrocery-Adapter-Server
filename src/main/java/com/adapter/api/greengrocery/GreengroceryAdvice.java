package com.adapter.api.greengrocery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GreengroceryAdvice {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> IOException(IOException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> ResponseStatusException(ResponseStatusException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.BAD_GATEWAY);
    }
}
