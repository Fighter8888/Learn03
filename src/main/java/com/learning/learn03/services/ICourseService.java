package com.learning.learn03.services;

import com.learning.learn03.base.IBaseService;
import com.learning.learn03.models.Course;

import java.util.List;

public interface ICourseService extends IBaseService<Course, Integer> {
    List<Course> findAllMajorCourses(String majorName);

}
