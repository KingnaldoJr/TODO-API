package dev.rmjr.todo.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotNull
    @Size(min = 10, max = 255)
    private String email;

    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 205)
    private String lastName;

    @NotNull
    @Size(min = 8, max = 32)
    private String password;

    @Size(min = 7, max = 20)
    private String phone;
}
