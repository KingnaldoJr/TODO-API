package dev.rmjr.todo.service;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.entity.VerificationToken;
import dev.rmjr.todo.exception.ExpiredVerificationTokenException;
import dev.rmjr.todo.exception.InvalidVerificationTokenException;
import dev.rmjr.todo.mapper.VerificationTokenMapper;
import dev.rmjr.todo.repository.UserRepository;
import dev.rmjr.todo.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository repository;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Spy
    @InjectMocks
    UserService service;

    VerificationTokenMapper tokenMapper;

    @BeforeEach
    void injectTokenMapper() {
        tokenMapper = spy(Mappers.getMapper(VerificationTokenMapper.class));
        ReflectionTestUtils.setField(service, "tokenMapper", tokenMapper);
    }

    @Test
    void shouldReturnUserByPrincipalTest() {
        Principal principal = mock(Principal.class);
        User expectedUser = User.builder()
                .id(0L)
                .email("reinaldomalinauskasjr@gmail.com")
                .firstName("Reinaldo")
                .lastName("Malinauskas Junior")
                .build();

        when(principal.getName()).thenReturn("reinaldomalinauskasjr@gmail.com");
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(expectedUser));

        User actualUser = service.getUserByPrincipal(principal);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldThrowUsernameNotFoundByPrincipalTest() {
        Principal principal = mock(Principal.class);

        when(principal.getName()).thenReturn("reinaldomalinauskasjr@gmail.com");
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.getUserByPrincipal(principal),
                "User not found with email: reinaldomalinauskasjr@gmail.com");
    }

    @Test
    void addVerificationTokenShouldReturnVerificationTokenWithUserTest() {
        ReflectionTestUtils.setField(service, "expiryTime", 24L);
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        User user = User.builder().build();
        VerificationToken expectedToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .build();

        when(tokenMapper.userAndTokenToVerificationToken(any(User.class), anyString(), anyLong()))
                .thenReturn(expectedToken);
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(expectedToken);

        VerificationToken actualToken = service.addVerificationTokenToUser(user, token);

        assertEquals(expectedToken.getToken(), actualToken.getToken());
        assertEquals(expectedToken.getUser(), actualToken.getUser());
    }

    @Test
    void getVerificationTokenShouldReturnTokenTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        VerificationToken expectedToken = VerificationToken.builder()
                .token(token)
                .build();

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(expectedToken));

        VerificationToken actualToken = service.getVerificationToken(token);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void getVerificationTokenShouldThrowInvalidVerificationTokenTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidVerificationTokenException.class,
                () -> service.getVerificationToken(token),
                "Invalid verification token: " + token);
    }

    @Test
    void confirmUserShouldReturnUserTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        LocalDateTime expiryTime = LocalDateTime.of(2022, 1, 5, 23, 32);
        LocalDateTime now = LocalDateTime.of(2022, 1, 5, 23, 30);
        User user = spy(User.builder()
                .firstName("Reinaldo")
                .build());
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryTime)
                .build();
        User expectedUser = User.builder()
                .firstName("Reinaldo")
                .enabled(true)
                .build();

        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            doReturn(verificationToken).when(service).getVerificationToken(anyString());
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);
            when(repository.save(any(User.class))).thenReturn(expectedUser);

            User actualUser = service.confirmUser(token);

            verify(user, times(1)).setEnabled(true);
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void confirmUserShouldThrowInvalidVerificationTokenTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";

        doThrow(new InvalidVerificationTokenException("Invalid verification token: " + token))
                .when(service).getVerificationToken(anyString());

        assertThrows(InvalidVerificationTokenException.class,
                () -> service.confirmUser(token),
                "Invalid verification token: " + token);
    }

    @Test
    void confirmUserShouldThrowExpiredVerificationTokenTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        LocalDateTime expiryTime = LocalDateTime.of(2022, 1, 5, 23, 29);
        LocalDateTime now = LocalDateTime.of(2022, 1, 5, 23, 30);
        User user = spy(User.builder()
                .firstName("Reinaldo")
                .build());
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryTime)
                .build();

        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            doReturn(verificationToken).when(service).getVerificationToken(anyString());
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            assertThrows(ExpiredVerificationTokenException.class,
                    () -> service.confirmUser(token),
                    "Expired token: " + token);
        }
    }
}
