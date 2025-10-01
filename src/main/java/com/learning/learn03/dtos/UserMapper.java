package com.learning.learn03.dtos;

import com.learning.learn03.models.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }
}
