package com.learning.learn03.repositories;

import com.learning.learn03.models.Course;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepositoryImplementation<Course, Integer> {
    Optional<Course> findCourseByCourseName(String courseName);
}
