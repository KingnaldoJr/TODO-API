package dev.rmjr.todo.service;

import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.exception.EmailExistsException;
import dev.rmjr.todo.exception.PhoneExistsException;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public User registerNewUserAccount(UserRegistrationRequest registrationDTO) {
        if(emailExists(registrationDTO.getEmail())) {
            throw new EmailExistsException("There is already a user with the email: " + registrationDTO.getEmail());
        }
        if(phoneExists(registrationDTO.getPhone())) {
            throw new PhoneExistsException("There is already a user with the phone: " + registrationDTO.getPhone());
        }

        User user = UserMapper.INSTANCE.userRegistrationRequestToUser(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return repository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        return repository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + principal.getName()));
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean phoneExists(String phone) {
        return repository.findByPhone(phone).isPresent();
    }
}
