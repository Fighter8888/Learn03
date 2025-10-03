package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account extends BaseEntity<Integer> {

    private UUID accountId;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
//    private UUID uuid;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    private Role role;
}
