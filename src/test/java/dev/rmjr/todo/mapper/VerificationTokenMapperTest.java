package dev.rmjr.todo.mapper;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.entity.VerificationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class VerificationTokenMapperTest {
    VerificationTokenMapper tokenMapper = Mappers.getMapper(VerificationTokenMapper.class);

    @Test
    void userAndTokenToVerificationTokenTest() {
        User expectedUser = User.builder().build();
        String expectedToken = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        long expirationTime = 24L;
        LocalDateTime localDateTime = LocalDateTime.of(2022, 1, 5, 15, 26);

        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTime);

            VerificationToken actualToken = tokenMapper
                    .userAndTokenToVerificationToken(expectedUser, expectedToken, expirationTime);

            assertEquals(expectedUser, actualToken.getUser());
            assertEquals(expectedToken, actualToken.getToken());
            assertEquals(localDateTime.plusHours(expirationTime), actualToken.getExpiryDate());
        }
    }
}
