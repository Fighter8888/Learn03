package com.learning.learn03.services;

import com.learning.learn03.base.IBaseService;
import com.learning.learn03.models.AvailableCourse;
import java.security.Principal;
import java.util.List;


public interface IAvailableCourseService extends IBaseService<AvailableCourse, Integer> {
    List<AvailableCourse> findAllTeacherCourse(Principal principal);
    List<AvailableCourse> findAllStudentCourses(Principal principal);
    List<AvailableCourse> findAllTermCourses(int termId, Principal principal);
}