package dev.rmjr.todo.mapper;

import dev.rmjr.todo.enums.Error;
import dev.rmjr.todo.response.GenericErrorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenericErrorResponseMapper {

    default GenericErrorResponse exceptionToGenericErrorResponse(Error error, RuntimeException exception) {
        return GenericErrorResponse.builder()
                .error(error.getCode())
                .status(error.getStatus().value())
                .message(exception.getMessage())
                .build();
    }
}
