package com.learning.learn03.repository;

import com.learning.learn03.model.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, Integer> {
}
