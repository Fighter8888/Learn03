package com.learning.learn03.controller;

import com.learning.learn03.model.Course;
import com.learning.learn03.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody Course req) {
        Course c = Course.builder()
                .code(req.getCode())
                .title(req.getTitle())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .capacity(req.getCapacity())
                .build();

        Course saved = service.createCourse(c);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Course>> listAll() {
        var list = service.listAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> get(@PathVariable int id) {
        return service.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{courseId}/assign-teacher/{teacherId}")
    public ResponseEntity<Course> assignTeacher(@PathVariable int courseId, @PathVariable int teacherId) {
        Course updated = service.assignTeacher(courseId, teacherId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Course> enroll(@PathVariable int courseId, @PathVariable int studentId) {
        Course updated = service.enrollStudent(courseId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Course> removeStudent(@PathVariable int courseId, @PathVariable int studentId) {
        Course updated = service.removeStudent(courseId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable int courseId) {
        service.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }


}