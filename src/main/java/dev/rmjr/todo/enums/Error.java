package dev.rmjr.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {
    BAD_CREDENTIALS("login-01", HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_FOUND("login-10", HttpStatus.UNAUTHORIZED),
    USER_TOKEN_GENERATION_FAILURE("login-20", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_EXISTS("register-01", HttpStatus.CONFLICT),
    PHONE_EXISTS("register-02", HttpStatus.CONFLICT),
    INVALID_VERIFICATION_TOKEN("register-10", HttpStatus.NOT_FOUND),
    EXPIRED_VERIFICATION_TOKEN("register-11", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String code;
    private final HttpStatus status;
}
