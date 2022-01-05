package dev.rmjr.todo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void getUsernameShouldReturnEmailTest() {
        String expectedUsername = "reinaldomalinauskasjr@gmail.com";
        User user = User.builder().email(expectedUsername).build();

        String actualUsername = user.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void isAccountNonExpiredShouldReturnTrueTest() {
        User user = User.builder().build();

        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLockedShouldReturnTrueTest() {
        User user = User.builder().build();

        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpiredShouldReturnTrueTest() {
        User user = User.builder().build();

        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabledShouldReturnEnabledTest() {
        User user = User.builder().enabled(false).build();

        assertFalse(user.isEnabled());
    }
}
