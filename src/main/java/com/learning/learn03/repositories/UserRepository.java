package com.learning.learn03.repositories;

import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);

    Optional<User> findByRoles(Role role);
//    @Query("SELECT u FROM User u JOIN u.roles r " +
//            "WHERE r.name = :roleName AND " +
//            "(u.firstName LIKE CONCAT('%', :firstName, '%') " +
//            "OR u.lastName LIKE CONCAT('%', :lastName, '%') " +
//            "OR u.email LIKE CONCAT('%', :email, '%'))")
//    List<User> findByRoleAndNameContaining(@Param ("roleName")String roleName,@Param("name") String name);
}
