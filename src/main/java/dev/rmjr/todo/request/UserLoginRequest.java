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
public class UserLoginRequest {

    @Schema(description = "User's email used in registration", required = true, example = "example@rmjr.dev")
    @NotNull
    @NotBlank
    @Size(min = 10, max = 255)
    @Pattern(regexp = Patterns.EMAIL)
    private String email;

    @Schema(description = "User's password", required = true, example = "12P@ssword3")
    @NotNull
    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = Patterns.PASSWORD)
    private String password;
}
