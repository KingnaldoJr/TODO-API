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
}
