package com.learning.learn03.interfaces;

import com.learning.learn03.base.IBaseService;
import com.learning.learn03.models.Course;

import java.util.List;

public interface ICourseService extends IBaseService<Course, Integer> {

    List<Course> findAllMajorCourses(String majorName);

//    public void createCourse(CourseDto course);
//
//    void updateCourse(int id, CourseDto courseRequestDto);
//
//    List<CourseDto> findAll();
//
//    public Optional<Course> findById(int id);
//
//    public List<Course> findByTeacher(int teacherId);
//
//    public Course updateCourseTeacher(int courseId, int teacherId);
//
//    public void addStudentToCourse(int courseId, int studentId);
//
//    public void deleteStudentFromCourse(int courseId, int studentId);
//
//    public void deleteCourse(int courseId);
}
