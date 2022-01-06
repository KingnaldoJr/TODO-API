package dev.rmjr.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {
    BAD_CREDENTIALS("login-01", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final HttpStatus status;
}
