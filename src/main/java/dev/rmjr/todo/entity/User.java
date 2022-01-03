package dev.rmjr.todo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(0)
    @Max(Long.MAX_VALUE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 205)
    @Column(name = "last_name", length = 205, nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Size(min = 7, max = 20)
    @Column(name = "phone", length = 20, nullable = false, unique = true)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
