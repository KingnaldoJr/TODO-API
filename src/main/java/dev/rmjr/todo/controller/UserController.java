package dev.rmjr.todo.controller;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.response.UserResponse;
import dev.rmjr.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        User user = service.registerNewUserAccount(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/v1/users/{email}")
                .buildAndExpand(user.getEmail())
                .toUri();

        return ResponseEntity.created(location).body(UserMapper.INSTANCE.userToUserResponse(user));
    }
}
