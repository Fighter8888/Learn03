package com.learning.learn03.interfaces;

import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;

import java.util.List;

public interface IPrincipalService {
    public List<User> getPendingUsers();

    public User updateUserStatus(int id, UserStatus newStatus);

    public User updateUser(int id, User updatedUser);
}
