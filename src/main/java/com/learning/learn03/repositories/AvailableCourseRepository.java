package com.learning.learn03.repositories;

import com.learning.learn03.models.AvailableCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableCourseRepository extends JpaRepository<AvailableCourse, Integer> {
}
