package com.learning.learn03.service;

import com.learning.learn03.model.User;
import com.learning.learn03.model.UserStatus;
import com.learning.learn03.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .filter(u -> u.getStatus() == UserStatus.Approved)
                .orElseThrow(() -> new RuntimeException("Invalid credentials or user not approved"));
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
