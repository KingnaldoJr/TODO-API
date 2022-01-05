package dev.rmjr.todo.event.listener;

import dev.rmjr.todo.event.OnUserRegistrationCompleteEvent;
import dev.rmjr.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.UUID;

@EnableAsync
@RequiredArgsConstructor
@Component
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String email;

    @Async
    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
        String token = UUID.randomUUID().toString();
        userService.addVerificationTokenToUser(event.getUser(), token);

        String confirmationUrl = event.getAppUrl() + "/api/v1/confirm?token=" + token;
        String message = "This is a verification email for your account in RMJR TODO, please access "
                + confirmationUrl
                + " to verify your account. If you don't created this account, please just ignore this email.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo(event.getUser().getEmail());
        mailMessage.setSubject("RMJR TODO - Email Confirmation");
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
