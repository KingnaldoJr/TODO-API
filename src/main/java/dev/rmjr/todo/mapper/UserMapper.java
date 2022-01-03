package dev.rmjr.todo.mapper;

import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    User userRegistrationRequestToUser(UserRegistrationRequest user);
}
