package dev.rmjr.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PhoneExistsException extends RuntimeException {

    public PhoneExistsException(String message) {
        super(message);
    }
}
