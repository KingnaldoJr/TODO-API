package dev.rmjr.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rmjr.todo.entity.User;
import dev.rmjr.todo.response.UserResponse;
import dev.rmjr.todo.service.TokenService;
import dev.rmjr.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.security.Principal;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @Mock
    TokenService tokenService;

    @InjectMocks
    UserController controller;

    ObjectMapper objectMapper;

    @BeforeEach
    public void loadObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnUserUsingPrincipalTest() throws JsonProcessingException {
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

        given()
                .standaloneSetup(controller)
                .auth().principal(principal)
        .when()
                .get("/api/v1/users")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(objectMapper.writeValueAsString(expectedResponse)));
    }
}
