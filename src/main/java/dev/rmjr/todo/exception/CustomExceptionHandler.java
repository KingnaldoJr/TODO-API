package dev.rmjr.todo.exception;

import dev.rmjr.todo.enums.Error;
import dev.rmjr.todo.mapper.GenericErrorResponseMapper;
import dev.rmjr.todo.response.GenericErrorResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final GenericErrorResponseMapper mapper = Mappers.getMapper(GenericErrorResponseMapper.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapper.exceptionToGenericErrorResponse(Error.BAD_CREDENTIALS, exception));
    }
}
