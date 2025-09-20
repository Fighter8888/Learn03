package com.learning.learn03.service;

import com.learning.learn03.model.User;
import com.learning.learn03.model.UserStatus;
import com.learning.learn03.repository.UserRepository;

public class PrincipalService {

    private final UserRepository userRepository;

    public PrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User approveUser(int id) {
        return userRepository.findById(id).map(user -> {
            if (user.getStatus() == UserStatus.Pending) {
                user.setStatus(UserStatus.Approved);
                return userRepository.save(user);
            }
            return user;
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}