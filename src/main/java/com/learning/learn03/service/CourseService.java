package com.learning.learn03.service;

import com.learning.learn03.model.*;
import com.learning.learn03.repository.CourseRepository;
import com.learning.learn03.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(Course course) {
        courseRepository.findByCode(course.getCode()).ifPresent(c -> {
            throw new IllegalArgumentException("Course code already exists");
        });
        return courseRepository.save(course);
    }

    public List<Course> listAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(int id) {
        return courseRepository.findById(id);
    }

    public List<Course> findByTeacher(int teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public Course assignTeacher(int courseId, int teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        // بررسی نقش یا وضعیت (در صورت استفاده از Role enum)
        // فرض می‌کنیم caller مسئول بررسی نقش است؛ اما برای امنیت بیشتر می‌توان بررسی کرد:
        // if (!teacher.getRole().equals(UserRole.TEACHER)) throw...

        if (teacher.getStatus() != UserStatus.Approved) {
            throw new IllegalStateException("Teacher not approved");
        }

        course.setTeacher((Teacher) teacher);
        return courseRepository.save(course);
    }

    @Transactional
    public Course enrollStudent(int courseId, int studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        if (student.getStatus() != UserStatus.Approved) {
            throw new IllegalStateException("Student not approved");
        }

        if (course.getCapacity() > 0 && course.getStudents().size() >= course.getCapacity()) {
            throw new IllegalStateException("Course is full");
        }

        course.getStudents().add((Student) student);
        return courseRepository.save(course);
    }

    @Transactional
    public Course removeStudent(int courseId, int studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        course.getStudents().remove(student);
        return courseRepository.save(course);
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }
}
