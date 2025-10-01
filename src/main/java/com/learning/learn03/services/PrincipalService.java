package com.learning.learn03.services;

import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.dtos.UserMapper;
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

    public List<UserDto> getPendingUsers() {
        return userRepository.findByStatus(UserStatus.Pending)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto updateUserStatus(int id, UserStatus newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(newStatus);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public UserDto updateUser(int id, UserDto updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        return UserMapper.toDto(userRepository.save(user));
    }
}