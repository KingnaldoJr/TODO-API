package dev.rmjr.todo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @Schema(description = "User's email", example = "example@rmjr.dev")
    private String email;

    @Schema(description = "User's first name", example = "Reinaldo")
    private String firstName;

    @Schema(description = "User's last name", example = "Malinauskas Junior")
    private String lastName;

    @Schema(description = "User's phone number", example = "+15551245652")
    private String phone;
}
