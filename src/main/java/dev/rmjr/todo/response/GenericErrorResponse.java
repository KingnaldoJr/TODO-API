package dev.rmjr.todo.response;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GenericErrorResponse {
    private String error;
    private Integer status;
    private String message;
}
