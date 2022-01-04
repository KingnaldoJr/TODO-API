package dev.rmjr.todo.service;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService service;

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
}
