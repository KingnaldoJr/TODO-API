package dev.rmjr.todo.event.listener;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.entity.VerificationToken;
import dev.rmjr.todo.event.OnUserRegistrationCompleteEvent;
import dev.rmjr.todo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnUserRegistrationCompleteListenerTest {

    @Mock
    UserService userService;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    OnUserRegistrationCompleteListener listener;

    @Captor
    ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @Test
    void shouldAddTokenToUserAndSendEmailTest() {
        User user = User.builder()
                .email("reinaldomalinauskasjr@gmail.com")
                .build();
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .build();
        OnUserRegistrationCompleteEvent event = new OnUserRegistrationCompleteEvent(user, "https://todo.rmjr.dev");

        ReflectionTestUtils.setField(listener, "email", "todo@rmjr.dev");
        when(userService.addVerificationTokenToUser(any(User.class), anyString())).thenReturn(verificationToken);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        listener.onApplicationEvent(event);

        verify(userService, times(1)).addVerificationTokenToUser(eq(user), anyString());
        verify(mailSender).send(mailMessageCaptor.capture());

        assertEquals("todo@rmjr.dev", mailMessageCaptor.getValue().getFrom());
        assertEquals("reinaldomalinauskasjr@gmail.com", mailMessageCaptor.getValue().getTo()[0]);
        assertEquals("RMJR TODO - Email Confirmation", mailMessageCaptor.getValue().getSubject());
    }
}
