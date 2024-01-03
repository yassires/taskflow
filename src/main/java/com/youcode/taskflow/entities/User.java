package com.youcode.taskflow.entities;


import com.youcode.taskflow.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private Integer token;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
