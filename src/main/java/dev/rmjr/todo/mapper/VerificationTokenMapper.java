package dev.rmjr.todo.mapper;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.entity.VerificationToken;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface VerificationTokenMapper {

    default VerificationToken userAndTokenToVerificationToken(User user, String token, Long expirationTime) {
        return VerificationToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(expirationTime))
                .build();
    }
}
