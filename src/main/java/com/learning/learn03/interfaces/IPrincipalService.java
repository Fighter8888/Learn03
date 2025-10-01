package com.learning.learn03.interfaces;

import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;

import java.util.List;

public interface IPrincipalService {
    public List<UserDto> getPendingUsers();

    public UserDto updateUserStatus(int id, UserStatus newStatus);

    public UserDto updateUser(int id, UserDto updatedUser);
}
