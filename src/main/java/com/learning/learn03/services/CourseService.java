package com.learning.learn03.services;

import com.learning.learn03.base.BaseService;
import com.learning.learn03.interfaces.ICourseService;
import com.learning.learn03.models.*;
import com.learning.learn03.repositories.CourseRepository;
import com.learning.learn03.repositories.MajorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CourseService extends BaseService<Course, Integer> implements ICourseService {

    private final CourseRepository courseRepository;
    private final MajorRepository majorRepository;

    protected CourseService(JpaRepository<Course, Integer> repository, CourseRepository courseRepository, MajorRepository majorRepository) {
        super(repository);
        this.courseRepository = courseRepository;
        this.majorRepository = majorRepository;
    }



    @Override
    protected void prePersist(Course course) {
        Major major = majorRepository.findById(course.getMajor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Major not found"));
        if (!major.isMajorActive()) {
            throw new EntityNotFoundException("Major not found");
        }
        if (courseRepository.existsByMajorAndTitle(course.getMajor().getId() , course.getCourseName())) {
            throw new RuntimeException("This course already exists in this major!");
        }
        course.setCourseExist(true);
    }

    @Override
    public void delete(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setCourseExist(false);
        courseRepository.save(course);
    }

    @Override
    public List<Course> findAllMajorCourses(String majorName) {
        Major major = majorRepository.findByMajorName(majorName)
                .orElseThrow(() -> new EntityNotFoundException("Major not found"));
        return major.getCourses();
    }

    @Override
    public Course findById(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        if (!course.isCourseExist()) {
            throw new EntityNotFoundException("Course not found");
        }
        return course;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = courseRepository.findAll();
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.isCourseExist()) {
                result.add(course);
            }
        }
        return result;
    }

}
