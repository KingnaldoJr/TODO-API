package dev.rmjr.todo.service;

import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.exception.EmailExistsException;
import dev.rmjr.todo.exception.PhoneExistsException;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(),
                true, true, true, true,
                List.of(new SimpleGrantedAuthority("USER")));
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    private boolean phoneExists(String phone) {
        return repository.findByPhone(phone).isPresent();
    }
}
