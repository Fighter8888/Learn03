package com.learning.learn03.controllers;

import com.learning.learn03.dtos.CourseDto;
import com.learning.learn03.models.Course;
import com.learning.learn03.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController( CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDto);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> findAll() {
        var list = courseService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> get(@PathVariable int id) {
        return courseService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{courseId}/assign-teacher/{teacherId}")
    public ResponseEntity<Course> assignTeacher(@PathVariable int courseId, @PathVariable int teacherId) {
        Course updated = courseService.updateCourseTeacher(courseId, teacherId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Course> enroll(@PathVariable int courseId, @PathVariable int studentId) {
        Course updated = courseService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Course> removeStudent(@PathVariable int courseId, @PathVariable int studentId) {
        Course updated = courseService.removeStudent(courseId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }


}