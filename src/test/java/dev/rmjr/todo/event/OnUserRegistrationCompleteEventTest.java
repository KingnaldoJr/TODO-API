package dev.rmjr.todo.event;

import dev.rmjr.todo.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OnUserRegistrationCompleteEventTest {

    @Test
    void shouldConstructEventTest() {
        User user = User.builder()
                .email("reinaldomalinauskasjr@gmail.com")
                .build();

        OnUserRegistrationCompleteEvent actualEvent =
                new OnUserRegistrationCompleteEvent(user, "https://todo.rmjr.dev");

        assertEquals("reinaldomalinauskasjr@gmail.com", actualEvent.getUser().getEmail());
        assertEquals("https://todo.rmjr.dev", actualEvent.getAppUrl());
    }
}
