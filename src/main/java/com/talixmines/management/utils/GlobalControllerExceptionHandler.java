package com.talixmines.management.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.talixmines.management.exception.EntityNotFoundException;
import com.talixmines.management.exception.ForbiddenActionException;
import com.talixmines.management.exception.ResourceAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error invalidInput(final MethodArgumentNotValidException e) {

        log.error(e.getMessage());

        final Error error = new Error();
        error.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        error.setMessage("unable to process the contained instructions");
        error.setErrors(
                e.getBindingResult().getFieldErrors().stream().map(objectError -> objectError.getField() + " " + objectError.getDefaultMessage()).collect(Collectors.toList()));

        return error;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Error notFound(final EntityNotFoundException e) {

        log.error(e.getMessage());

        final Error error = new Error();
        error.setCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());

        return error;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public Error conflict(final ResourceAlreadyExistException e) {

        log.error(e.getMessage());

        final Error error = new Error();
        error.setCode(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());

        return error;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenActionException.class)
    public Error conflict(final ForbiddenActionException e) {

        log.error(e.getMessage());

        final Error error = new Error();
        error.setCode(HttpStatus.FORBIDDEN.value());
        error.setMessage(e.getMessage());

        return error;
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(IllegalStateException.class)
    public Error illegalState(final IllegalStateException e) {

        log.error(e.getMessage());

        final Error error = new Error();
        error.setCode(HttpStatus.EXPECTATION_FAILED.value());
        error.setMessage(e.getMessage());

        return error;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Error internalError(final Exception e) {

        log.error("internalError", e);

        final Error error = new Error();
        error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("An unexpected server error occurred.");

        return error;
    }

    @Data
    class Error {
        private Integer code;
        private String message;
        private List<String> errors;
    }
}

