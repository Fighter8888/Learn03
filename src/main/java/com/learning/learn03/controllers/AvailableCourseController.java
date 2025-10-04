package com.learning.learn03.controllers;

import com.learning.learn03.dtos.AvailableCourseDTO;
import com.learning.learn03.interfaces.IAvailableCourseService;
import com.learning.learn03.mappers.AvailableCourseMapper;
import com.learning.learn03.models.AvailableCourse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/web/availableCourse")
public class AvailableCourseController {

    private final IAvailableCourseService iavailableCourseService;
    private final AvailableCourseMapper availableCourseMapper;
    private final ResponseOfferedCourseMapper respMapper;

    public AvailableCourseController(IAvailableCourseService iavailableCourseService, AvailableCourseMapper availableCourseMapper, ResponseOfferedCourseMapper respMapper) {
        this.iavailableCourseService = iavailableCourseService;
        this.availableCourseMapper = availableCourseMapper;
        this.respMapper = respMapper;
    }


    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseOfferedCourseDTO> create(@RequestBody AvailableCourseDTO dto) {
        AvailableCourse availableCourse = iavailableCourseService.persist(availableCourseMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(respMapper.toDto(availableCourse));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseOfferedCourseDTO> update(@PathVariable int id, @RequestBody AvailableCourseDTO dto) {
        AvailableCourse foundedCourse = iavailableCourseService.findById(id);
        foundedCourse.setACourseEndDate(dto.getEndTime());
        foundedCourse.setCapacity(dto.getCapacity());
        foundedCourse.setACourseStartDate(dto.getStartTime());
        AvailableCourse availableCourse = iavailableCourseService.persist(foundedCourse);
        return ResponseEntity.ok(respMapper.toDto(availableCourse));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable Integer id) {
        iavailableCourseService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO("Course deleted success." , true));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseOfferedCourseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(respMapper.toDto(iavailableCourseService.findById(id)));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ResponseOfferedCourseDTO>> findAll() {
        List<ResponseOfferedCourseDTO> courseDTOS = new ArrayList<>();
        for (AvailableCourse course : iavailableCourseService.findAll()) courseDTOS.add(respMapper.toDto(course));
        return ResponseEntity.ok(courseDTOS);
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/courses")
    public ResponseEntity<List<ResponseOfferedCourseDTO>> findAllTeacherCourses(Principal principal) {
        List<ResponseOfferedCourseDTO> courseDTOS = new ArrayList<>();
        for (AvailableCourse course : iavailableCourseService.findAllTeacherCourse(principal)) courseDTOS.add(respMapper.toDto(course));
        return ResponseEntity.ok(courseDTOS);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/courses")
    public ResponseEntity<List<ResponseOfferedCourseDTO>> findAllStudentCourses(Principal principal) {
        List<AvailableCourse> studentCourses = iavailableCourseService.findAllStudentCourses(principal);
        List<ResponseOfferedCourseDTO> courses = new ArrayList<>();
        for (AvailableCourse course : studentCourses) {
            ResponseOfferedCourseDTO dto = respMapper.toDto(course);
            courses.add(dto);
        }
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/term/courses/{termId}")
    public ResponseEntity<List<ResponseOfferedCourseDTO>> findAllTermCourses(@PathVariable Integer termId, Principal principal) {
        List<AvailableCourse> termCourses = iavailableCourseService.findAllTermCourses(termId , principal);
        List<ResponseOfferedCourseDTO> courses = new ArrayList<>();
        for (AvailableCourse course : termCourses) {
            ResponseOfferedCourseDTO dto = respMapper.toDto(course);
            courses.add(dto);
        }
        return ResponseEntity.ok(courses);
    }
}
