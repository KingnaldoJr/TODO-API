package dev.rmjr.todo.request;

import dev.rmjr.todo.util.Patterns;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotNull
    @NotBlank
    @Size(min = 10, max = 255)
    @Pattern(regexp = Patterns.EMAIL)
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = Patterns.FIRST_NAME)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 205)
    @Pattern(regexp = Patterns.LAST_NAME)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = Patterns.PASSWORD)
    private String password;

    @NotBlank
    @Size(min = 7, max = 20)
    @Pattern(regexp = Patterns.PHONE)
    private String phone;
}
