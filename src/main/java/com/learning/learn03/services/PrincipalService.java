package com.learning.learn03.services;

import com.learning.learn03.interfaces.IPrincipalService;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import com.learning.learn03.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrincipalService implements IPrincipalService {

    private final UserRepository userRepository;

    public PrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getPendingUsers() {
        List<User> users = userRepository.findByStatus((UserStatus.Pending));
        return users;
    }

    public User updateUserStatus(int id, UserStatus newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(newStatus);
        return userRepository.save(user);
    }

    public User updateUser(int id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRoles(updatedUser.getRoles());
        return userRepository.save(user);
    }
}