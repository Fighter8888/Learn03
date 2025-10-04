package com.learning.learn03.repositories;

import com.learning.learn03.models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepositoryImplementation<Course, Integer> {
    Optional<Course> findCourseByCourseName(String courseName);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Course c " +
            "WHERE c.courseName = :title AND c.major.id = :majorId")
    boolean existsByMajorAndTitle(@Param("majorId") Integer majorId, @Param("title") String title);

}
