package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import com.learning.learn03.models.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account extends BaseEntity<Integer> {

    private UUID accountAuthId;
    @Email
    @NotBlank
    @Column(unique = true)
    private String userName;
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
