package com.github.efitapia.payservice.api.advice;

import com.github.efitapia.payservice.api.model.ErrorResponse;
import com.github.efitapia.payservice.repo.NoSuchEntityException;
import com.github.efitapia.payservice.service.NotEnoughMoneyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchEntityException.class)
    public ErrorResponse handle(NoSuchEntityException e) {
        log.error("Entity not found", e);
        return new ErrorResponse()
            .setCode("1")
            .setMessage(e.getMessage());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ErrorResponse handle(NotEnoughMoneyException e) {
        log.error("Not enough money", e);
        return new ErrorResponse()
            .setCode("2")
            .setMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handle(Exception e) {
        log.error("Unexpected error", e);
        return new ErrorResponse()
            .setCode("0")
            .setMessage("Unexpected error");
    }

}

