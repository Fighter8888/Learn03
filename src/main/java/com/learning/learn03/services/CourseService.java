//package com.learning.learn03.services;
//
//import com.learning.learn03.dtos.CourseDto;
//import com.learning.learn03.interfaces.ICourseService;
//import com.learning.learn03.models.*;
//import com.learning.learn03.repositories.CourseRepository;
//import com.learning.learn03.repositories.StudentRepository;
//import com.learning.learn03.repositories.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.stereotype.Service;
//import java.util.*;
//
//@Service
//public class CourseService implements ICourseService {
//    private final CourseRepository courseRepository;
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//
//    public CourseService(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository) {
//        this.courseRepository = courseRepository;
//        this.userRepository = userRepository;
//        this.studentRepository = studentRepository;
//    }
//
//    @Override
//    public void createCourse(CourseDto dto) {
//        courseRepository.save(Course.builder()
//                .title(dto.getTitle())
//                .code(dto.getCode())
//                .startDate(dto.getStartDate())
//                .endDate(dto.getEndDate())
//                .build());
//    }
//
//    @Override
//    public void updateCourse(int id, CourseDto courseDto) {
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Course with this id not found :" + id));
//        course.setTitle(courseDto.getTitle());
//        course.setCourseCode(courseDto.getCode());
//        course.setStartDate(courseDto.getStartDate());
//        course.setEndDate(courseDto.getEndDate());
//        courseRepository.save(course);
//    }
//
//    @Override
//    public List<CourseDto> findAll() {
//        List<Course> courses = courseRepository.findAll();
//        List<CourseDto> courseDtoList = new ArrayList<>();
//        for (Course course : courses) {
//            CourseDto dto = new CourseDto();
//            dto.setTitle(course.getTitle());
//            dto.setCode(course.getCourseCode());
//            dto.setStartDate(course.getStartDate());
//            dto.setEndDate(course.getEndDate());
//            courseDtoList.add(dto);
//        }
//        return courseDtoList;
//    }
//
//    @Override
//    public Optional<Course> findById(int id) {
//        return courseRepository.findById(id);
//    }
//
//    @Override
//    public List<Course> findByTeacher(int teacherId) {
//        return courseRepository.findByTeacherId(teacherId);
//    }
//
//    @Override
//    public Course updateCourseTeacher(int courseId, int teacherId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NoSuchElementException("Course not found"));
//        User teacher = userRepository.findById(teacherId)
//                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));
//
//
//        if (teacher.getStatus() != UserStatus.Approved) {
//            throw new IllegalStateException("Teacher not approved");
//        }
//
//        course.setTeacher((Teacher) teacher);
//        return courseRepository.save(course);
//    }
//
//    @Override
//    public void addStudentToCourse(int courseId, int studentId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NoSuchElementException("Course not found"));
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new NoSuchElementException("Student not found"));
//
//        if (student.getStatus() != UserStatus.Approved) {
//            throw new IllegalStateException("Student not approved");
//        }
//
//        if (course.getCapacity() > 0 && course.getStudents().size() >= course.getCapacity()) {
//            throw new IllegalStateException("Course is full");
//        }
//
//        course.getStudents().add(student);
//        student.getCourse().add(course);
//        studentRepository.save(student);
//    }
//
//    @Override
//    public void deleteStudentFromCourse(int courseId, int studentId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new NoSuchElementException("Course not found"));
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new NoSuchElementException("Student not found"));
//
//        if (student.getStatus() != UserStatus.Approved) {
//            throw new IllegalStateException("Student not approved");
//        }
//
//        if (course.getCapacity() > 0 && course.getStudents().size() >= course.getCapacity()) {
//            throw new IllegalStateException("Course is full");
//        }
//
//        course.getStudents().remove(student);
//        student.getCourse().remove(course);
//        studentRepository.save(student);
//    }
//
//
//
//    @Override
//    public void deleteCourse(int courseId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new EntityNotFoundException("Course not found :" + courseId));
//        courseRepository.delete(course);
//    }
//}
