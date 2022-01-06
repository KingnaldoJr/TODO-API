package dev.rmjr.todo.service;

import dev.rmjr.todo.entity.VerificationToken;
import dev.rmjr.todo.event.OnUserRegistrationCompleteEvent;
import dev.rmjr.todo.exception.ExpiredVerificationTokenException;
import dev.rmjr.todo.exception.InvalidVerificationTokenException;
import dev.rmjr.todo.mapper.VerificationTokenMapper;
import dev.rmjr.todo.repository.VerificationTokenRepository;
import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.exception.EmailExistsException;
import dev.rmjr.todo.exception.PhoneExistsException;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenMapper tokenMapper = Mappers.getMapper(VerificationTokenMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Value("${rmjr.registration.expiration-time}")
    Long expiryTime;

    public User registerNewUserAccount(UserRegistrationRequest registrationDTO) {
        if(emailExists(registrationDTO.getEmail())) {
            throw new EmailExistsException("There is already a user with the email: " + registrationDTO.getEmail());
        }
        if(phoneExists(registrationDTO.getPhone())) {
            throw new PhoneExistsException("There is already a user with the phone: " + registrationDTO.getPhone());
        }

        User user = userMapper.userRegistrationRequestToUser(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user = repository.save(user);

        eventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user,
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()));

        return user;
    }

    public User getUserByPrincipal(Principal principal) {
        return repository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + principal.getName()));
    }

    public VerificationToken addVerificationTokenToUser(User user, String token) {
        return verificationTokenRepository.save(
                tokenMapper.userAndTokenToVerificationToken(user, token, expiryTime));
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Invalid verification token: " + token));
    }

    public User confirmUser(String token) {
        VerificationToken verificationToken = getVerificationToken(token);

        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredVerificationTokenException("Expired token: " + token);
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean phoneExists(String phone) {
        return phone != null && repository.findByPhone(phone).isPresent();
    }
}
