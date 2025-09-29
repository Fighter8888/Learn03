package com.learning.learn03.interfaces;

import com.learning.learn03.dtos.CourseDto;
import com.learning.learn03.models.Course;

import java.util.List;
import java.util.Optional;

public interface ICourseService {
    public void createCourse(CourseDto course);

    void updateCourse(int id, CourseDto courseRequestDto);

    List<CourseDto> findAll();

    public Optional<Course> findById(int id);

    public List<Course> findByTeacher(int teacherId);

    public Course updateCourseTeacher(int courseId, int teacherId);

    public void addStudentToCourse(int courseId, int studentId);

    public void deleteStudentFromCourse(int courseId, int studentId);

    public void deleteCourse(int courseId);
}
