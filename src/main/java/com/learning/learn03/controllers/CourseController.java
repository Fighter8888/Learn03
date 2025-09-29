package com.learning.learn03.controllers;

import com.learning.learn03.dtos.CourseDto;
import com.learning.learn03.models.Course;
import com.learning.learn03.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('Principal')")
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDto);
    }

    @PreAuthorize("hasRole('Principal')")
    @PostMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable int id, @RequestBody CourseDto courseDto) {
        courseService.updateCourse(id, courseDto);
        return ResponseEntity.status(HttpStatus.OK).body(courseDto);
    }

    @PreAuthorize("hasRole('Principal')")
    @GetMapping
    public ResponseEntity<List<CourseDto>> findAllCourses() {
        List<CourseDto> courseList = courseService.findAll();
        return ResponseEntity.ok(courseList);
    }

    @PreAuthorize("hasRole('Principal')")
    @GetMapping("/{id}")
    public ResponseEntity<Course> findCourseById(@PathVariable int id) {
        return courseService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('Principal')")
    @PutMapping("/{courseId}/assign-teacher/{teacherId}")
    public ResponseEntity<CourseDto> updateCourseTeacher(@PathVariable int courseId, @PathVariable int teacherId) {
        courseService.updateCourseTeacher(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/Add-student/{studentId}")
    public ResponseEntity<CourseDto> addStudent(@PathVariable int courseId, @PathVariable int studentId) {
        courseService.addStudentToCourse(courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{courseId}/remove-student/{studentId}")
    public ResponseEntity<Course> removeStudent(@PathVariable int courseId, @PathVariable int studentId) {
        courseService.deleteStudentFromCourse(courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}