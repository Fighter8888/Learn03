package com.learning.learn03.controllers;

import com.learning.learn03.dtos.CourseDto;
import com.learning.learn03.mappers.CourseMapper;
import com.learning.learn03.models.Course;
import com.learning.learn03.services.Implements.CourseServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/web/course")
public class CourseController {
    private final CourseServiceImpl courseServiceImpl;
    private final CourseMapper courseMapper;

    public CourseController(CourseServiceImpl courseServiceImpl, CourseMapper courseMapper) {
        this.courseServiceImpl = courseServiceImpl;
        this.courseMapper = courseMapper;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CourseDto> save(@RequestBody CourseDto dto) {
        Course course = courseServiceImpl.persist(courseMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseMapper.toDto(course));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> update(@PathVariable int id, @RequestBody CourseDto dto) {
        Course foundedCourse = courseServiceImpl.findById(id);
        foundedCourse.setCourseName(dto.getTitle());
        foundedCourse.setCourseCode(dto.getCourseCode());
        Course course = courseServiceImpl.persist(foundedCourse);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        courseServiceImpl.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findById(@PathVariable int id) {
        return ResponseEntity.ok(courseMapper.toDto(courseServiceImpl.findById(id)));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CourseDto>> findAll() {
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseServiceImpl.findAll()) courseDtos.add(courseMapper.toDto(course));
        return ResponseEntity.ok(courseDtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/major/courses")
    public ResponseEntity<List<CourseDto>> findAllMajorCourses(@RequestBody String majorName) {
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseServiceImpl.findAllMajorCourses(majorName)) courseDtos.add(courseMapper.toDto(course));
        return ResponseEntity.ok(courseDtos);
    }
}
