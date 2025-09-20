package com.learning.learn03.repository;

import com.learning.learn03.model.Course;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepositoryImplementation<Course, Integer> {
    Optional<Course> findByCode(int code);
    List<Course> findByTeacherId(int teacherId);
}
