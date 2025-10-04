package com.learning.learn03.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleRequest {
    private int personId;
    private String role;
}
