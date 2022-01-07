package dev.rmjr.todo.exception;

import dev.rmjr.todo.enums.Error;
import dev.rmjr.todo.mapper.GenericErrorResponseMapper;
import dev.rmjr.todo.response.GenericErrorResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final GenericErrorResponseMapper mapper = Mappers.getMapper(GenericErrorResponseMapper.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(Error.BAD_CREDENTIALS.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.BAD_CREDENTIALS, exception));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleUsernameNotFound(UsernameNotFoundException exception) {
        return ResponseEntity.status(Error.USERNAME_NOT_FOUND.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.USERNAME_NOT_FOUND, exception));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<GenericErrorResponse> handleEmailExists(EmailExistsException exception) {
        return ResponseEntity.status(Error.EMAIL_EXISTS.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.EMAIL_EXISTS, exception));
    }

    @ExceptionHandler(PhoneExistsException.class)
    public ResponseEntity<GenericErrorResponse> handlePhoneExists(PhoneExistsException exception) {
        return ResponseEntity.status(Error.PHONE_EXISTS.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.PHONE_EXISTS, exception));
    }

    @ExceptionHandler(ExpiredVerificationTokenException.class)
    public ResponseEntity<GenericErrorResponse> handleExpiredVerificationToken(ExpiredVerificationTokenException exception) {
        return ResponseEntity.status(Error.EXPIRED_VERIFICATION_TOKEN.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.EXPIRED_VERIFICATION_TOKEN, exception));
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<GenericErrorResponse> handleInvalidVerificationToken(InvalidVerificationTokenException exception) {
        return ResponseEntity.status(Error.INVALID_VERIFICATION_TOKEN.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.INVALID_VERIFICATION_TOKEN, exception));
    }

    @ExceptionHandler(UserTokenGenerationFailureException.class)
    public ResponseEntity<GenericErrorResponse> handleUserTokenGenerationFailure(UserTokenGenerationFailureException exception) {
        return ResponseEntity.status(Error.USER_TOKEN_GENERATION_FAILURE.getStatus())
                .body(mapper.exceptionToGenericErrorResponse(Error.USER_TOKEN_GENERATION_FAILURE, exception));
    }
}
