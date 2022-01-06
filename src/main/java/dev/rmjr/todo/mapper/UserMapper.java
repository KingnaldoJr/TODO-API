package dev.rmjr.todo.mapper;

import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", expression = "java(false)")
    User userRegistrationRequestToUser(UserRegistrationRequest user);
    UserResponse userToUserResponse(User user);
}
