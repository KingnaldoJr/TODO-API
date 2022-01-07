package dev.rmjr.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserTokenGenerationFailureException extends RuntimeException {

    public UserTokenGenerationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
