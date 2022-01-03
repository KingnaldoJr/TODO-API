package dev.rmjr.todo.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @NotNull
    @Size(min = 10, max = 255)
    private String email;

    @NotNull
    @Size(min = 8, max = 32)
    private String password;
}
