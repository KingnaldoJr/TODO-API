package dev.rmjr.todo.controller;

import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.request.UserLoginRequest;
import dev.rmjr.todo.request.UserRegistrationRequest;
import dev.rmjr.todo.response.TokenResponse;
import dev.rmjr.todo.response.UserResponse;
import dev.rmjr.todo.service.TokenService;
import dev.rmjr.todo.service.UserService;
import dev.rmjr.todo.util.Patterns;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        User user = userService.registerNewUserAccount(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/v1/users/{email}")
                .buildAndExpand(user.getEmail())
                .toUri();

        return ResponseEntity.created(location).body(userMapper.userToUserResponse(user));
    }

    @PostMapping
    public ResponseEntity<TokenResponse> authenticate(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(tokenService.generateUserToken(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<UserResponse> confirmRegistration(@RequestParam(value = "token")
                                                                @Size(min = 36, max = 36)
                                                                @Pattern(regexp = Patterns.UUID)
                                                                @Valid String token) {
        return ResponseEntity.ok(userMapper.userToUserResponse(userService.confirmUser(token)));
    }

    @Operation(summary = "Get basic information from authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<UserResponse> getUserInformation(Principal principal) {
        return ResponseEntity.ok(userMapper.userToUserResponse(userService.getUserByPrincipal(principal)));
    }
}
