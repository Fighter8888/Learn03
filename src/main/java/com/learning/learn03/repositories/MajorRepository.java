package com.learning.learn03.repositories;

import com.learning.learn03.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findByMajorName(String majorName);
    boolean existsByMajorName(String majorName);

}
