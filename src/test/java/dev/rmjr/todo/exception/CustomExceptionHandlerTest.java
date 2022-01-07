package dev.rmjr.todo.exception;

import dev.rmjr.todo.enums.Error;
import dev.rmjr.todo.mapper.GenericErrorResponseMapper;
import dev.rmjr.todo.response.GenericErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @InjectMocks
    CustomExceptionHandler handler;

    GenericErrorResponseMapper mapper;

    @BeforeEach
    void loadMapper() {
        mapper = spy(Mappers.getMapper(GenericErrorResponseMapper.class));
        ReflectionTestUtils.setField(handler, "mapper", mapper);
    }

    @Test
    void handleBadCredentialsTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper).exceptionToGenericErrorResponse(any(Error.class), any(BadCredentialsException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleBadCredentials(new BadCredentialsException("Invalid Credentials"));

        assertEquals(HttpStatus.UNAUTHORIZED, actualResponse.getStatusCode());
    }

    @Test
    void handleUsernameNotFoundTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper).exceptionToGenericErrorResponse(any(Error.class), any(UsernameNotFoundException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleUsernameNotFound(new UsernameNotFoundException("User not found!"));

        assertEquals(HttpStatus.UNAUTHORIZED, actualResponse.getStatusCode());
    }

    @Test
    void handleEmailExistsTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper).exceptionToGenericErrorResponse(any(Error.class), any(EmailExistsException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleEmailExists(new EmailExistsException("Email already in use"));

        assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
    }

    @Test
    void handlePhoneExistsTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper).exceptionToGenericErrorResponse(any(Error.class), any(PhoneExistsException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handlePhoneExists(new PhoneExistsException("Phone already in use"));

        assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
    }

    @Test
    void handleExpiredVerificationTokenTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper)
                .exceptionToGenericErrorResponse(any(Error.class), any(ExpiredVerificationTokenException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleExpiredVerificationToken(new ExpiredVerificationTokenException("Verification token expired!"));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, actualResponse.getStatusCode());
    }

    @Test
    void handleInvalidVerificationTokenTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper)
                .exceptionToGenericErrorResponse(any(Error.class), any(InvalidVerificationTokenException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleInvalidVerificationToken(new InvalidVerificationTokenException("Verification token not found!"));

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    }

    @Test
    void handleUserTokenGenerationFailureTest() {
        GenericErrorResponse response = GenericErrorResponse.builder().build();

        doReturn(response)
                .when(mapper)
                .exceptionToGenericErrorResponse(any(Error.class), any(UserTokenGenerationFailureException.class));

        ResponseEntity<GenericErrorResponse> actualResponse = handler
                .handleUserTokenGenerationFailure(new UserTokenGenerationFailureException("File not found.", new Throwable()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }
}
