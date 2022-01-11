package dev.rmjr.todo.request;

import dev.rmjr.todo.util.Patterns;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "User's email", required = true, example = "example@rmjr.dev")
    @NotNull
    @NotBlank
    @Size(min = 10, max = 255)
    @Pattern(regexp = Patterns.EMAIL)
    private String email;

    @Schema(description = "User's firstname", required = true, example = "Reinaldo")
    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = Patterns.FIRST_NAME)
    private String firstName;

    @Schema(description = "User's lastname", required = true, example = "Malinauskas Junior")
    @NotNull
    @NotBlank
    @Size(min = 2, max = 205)
    @Pattern(regexp = Patterns.LAST_NAME)
    private String lastName;

    @Schema(description = "A strong password to protect user's account.", required = true, example = "12P@assword34")
    @NotNull
    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = Patterns.PASSWORD)
    private String password;

    @Schema(description = "User's phone number", example = "+15551245652")
    @Size(min = 7, max = 20)
    @Pattern(regexp = Patterns.PHONE)
    private String phone;
}
