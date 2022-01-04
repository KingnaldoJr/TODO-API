package dev.rmjr.todo.controller;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.request.UserLoginRequest;
import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.response.TokenResponse;
import dev.rmjr.todo.response.UserResponse;
import dev.rmjr.todo.service.TokenService;
import dev.rmjr.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        User user = userService.registerNewUserAccount(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/v1/users/{email}")
                .buildAndExpand(user.getEmail())
                .toUri();

        return ResponseEntity.created(location).body(UserMapper.INSTANCE.userToUserResponse(user));
    }

    @PostMapping
    public ResponseEntity<TokenResponse> authenticate(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(tokenService.generateUserToken(request));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserInformation(@AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserResponse(userService.getUserByPrincipal(principal)));
    }
}
