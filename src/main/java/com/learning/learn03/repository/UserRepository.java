package com.learning.learn03.repository;

import com.learning.learn03.model.Role;
import com.learning.learn03.model.User;
import com.learning.learn03.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);
}
