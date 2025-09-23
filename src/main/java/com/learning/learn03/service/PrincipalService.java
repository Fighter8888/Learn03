package com.learning.learn03.service;

import com.learning.learn03.model.User;
import com.learning.learn03.model.UserStatus;
import com.learning.learn03.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrincipalService {

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

//    public User approveUser(int id) {
//        return userRepository.findById(id).map(user -> {
//            if (user.getStatus() == UserStatus.Pending) {
//                user.setStatus(UserStatus.Approved);
//                return userRepository.save(user);
//            }
//            return user;
//        }).orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    public User rejectUser(int id) {
//        return userRepository.findById(id).map(user -> {
//            if (user.getStatus() == UserStatus.Pending) {
//                user.setStatus(UserStatus.Rejected);
//                return userRepository.save(user);
//            }
//            return user;
//        }).orElseThrow(() -> new RuntimeException("User not found"));
//    }

    public User updateUser(int id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }
}