package dev.rmjr.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.exception.CustomExceptionHandler;
import dev.rmjr.todo.exception.ExpiredVerificationTokenException;
import dev.rmjr.todo.exception.InvalidVerificationTokenException;
import dev.rmjr.todo.mapper.UserMapper;
import dev.rmjr.todo.response.UserResponse;
import dev.rmjr.todo.service.TokenService;
import dev.rmjr.todo.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @Mock
    TokenService tokenService;

    @InjectMocks
    UserController controller;

    @InjectMocks
    CustomExceptionHandler exceptionHandler;

    UserMapper userMapper;
    ObjectMapper objectMapper;

    @BeforeEach
    public void loadMappers() {
        RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
        userMapper = spy(Mappers.getMapper(UserMapper.class));
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(controller, "userMapper", userMapper);
    }

    @Test
    void getUserInformationShouldReturnUserTest() throws JsonProcessingException {
        Principal principal = mock(Principal.class);
        User user = User.builder()
                .email("reinaldomalinauskasjr@gmail.com")
                .firstName("Reinaldo")
                .lastName("Malinauskas Junior")
                .build();
        UserResponse expectedResponse = UserResponse.builder()
                .email("reinaldomalinauskasjr@gmail.com")
                .firstName("Reinaldo")
                .lastName("Malinauskas Junior")
                .build();

        when(userService.getUserByPrincipal(any(Principal.class))).thenReturn(user);
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(expectedResponse);

        given()
                .auth().principal(principal)
        .when()
                .get("/api/v1/users")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void getUserInformationReturnUsernameNotFoundExceptionTest() {
        Principal principal = mock(Principal.class);
        String email = "reinaldomalinauskasj@gmail.com";

        when(userService.getUserByPrincipal(any(Principal.class)))
                .thenThrow(new UsernameNotFoundException("User not found with email: " + email));

        given()
                .auth().principal(principal)
        .when()
                .get("/api/v1/users")
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("error", equalTo("login-10"));
    }

    @Test
    void confirmRegistrationShouldReturnUserTest() throws JsonProcessingException {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";
        UserResponse expectedResponse = UserResponse.builder()
                .firstName("Reinaldo")
                .build();

        when(userService.confirmUser(anyString())).thenReturn(User.builder().firstName("Reinaldo").build());
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(expectedResponse);

        given()
        .when()
                .get("/api/v1/users/confirm?token={}", token)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void confirmRegistrationShouldReturnInvalidVerificationTokenExceptionTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e4";

        when(userService.confirmUser(anyString()))
                .thenThrow(new InvalidVerificationTokenException("Invalid verification token: " + token));

        given()
        .when()
                .get("/api/v1/users/confirm?token={}", token)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", equalTo("register-10"));
    }

    @Test
    void confirmRegistrationShouldReturnExpiredVerificationTokenExceptionTest() {
        String token = "90d8c81e-75c7-473a-b268-3aa7791a51e3";

        when(userService.confirmUser(anyString()))
                .thenThrow(new ExpiredVerificationTokenException("Expired token: " + token));

        given()
                .when()
                .get("/api/v1/users/confirm?token={}", token)
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("error", equalTo("register-11"));
    }
}
