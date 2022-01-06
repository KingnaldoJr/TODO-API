package dev.rmjr.todo.mapper;

import dev.rmjr.todo.enums.Error;
import dev.rmjr.todo.response.GenericErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GenericErrorResponseMapperTest {
    GenericErrorResponseMapper mapper = Mappers.getMapper(GenericErrorResponseMapper.class);

    @Test
    void exceptionToGenericErrorResponseTest() {
        Error error = Error.BAD_CREDENTIALS;
        BadCredentialsException exception = new BadCredentialsException("Invalid password!");
        GenericErrorResponse expectedResponse = GenericErrorResponse.builder()
                .error(error.getCode())
                .status(error.getStatus().value())
                .message(exception.getMessage())
                .build();

        GenericErrorResponse actualResponse = mapper.exceptionToGenericErrorResponse(error, exception);

        assertEquals(expectedResponse, actualResponse);
    }
}
