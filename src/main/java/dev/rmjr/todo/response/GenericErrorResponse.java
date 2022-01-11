package dev.rmjr.todo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GenericErrorResponse {
    @Schema(description = "Internal error code", example = "login-01")
    private String error;

    @Schema(description = "HTTP Code Status", example = "404")
    private Integer status;

    @Schema(description = "Error message", example = "Email already in use.")
    private String message;
}
