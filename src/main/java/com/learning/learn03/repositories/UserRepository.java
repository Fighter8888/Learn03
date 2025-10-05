package com.learning.learn03.repositories;

import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByRoles(Role role);
}
