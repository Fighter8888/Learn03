package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.AvailableCourseDto;
import com.learning.learn03.models.AvailableCourse;
import com.learning.learn03.models.Course;
import com.learning.learn03.models.Semester;
import com.learning.learn03.models.User;
import com.learning.learn03.repositories.CourseRepository;
import com.learning.learn03.repositories.SemesterRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AvailableCourseMapper implements BaseMapper<AvailableCourse, AvailableCourseDto> {
    
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;


//    @Mapping(source = "aCourseStartDate", target = "startTime")
//    @Mapping(source = "aCourseEndDate", target = "endTime")
//    @Mapping(source = "Capacity", target = "capacity")
    public abstract AvailableCourseDto toDto(AvailableCourse entity);

//    @Mapping(source = "startTime", target = "CourseStartDate")
//    @Mapping(source = "endTime", target = "aCourseEndDate")
//    @Mapping(source = "capacity", target = "Capacity")
    public abstract AvailableCourse toEntity(AvailableCourseDto dto);

    @AfterMapping
    protected void afterToEntity(AvailableCourseDto dto, @MappingTarget AvailableCourse entity) {
        if (dto.getSemesterCode() != null) {
            Semester semester = semesterRepository.findById(dto.getSemesterCode())
                    .orElseThrow(() -> new EntityNotFoundException("not found"));
            entity.setSemester(semester);
        }
        if (dto.getCourseCode() != null) {
            Course course = courseRepository.findById(dto.getCourseCode())
                    .orElseThrow(() -> new EntityNotFoundException("not found"));
            entity.setCourse(course);
        }
        if (dto.getTeacherId() != null) {
            User user = userRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("not found"));
            Semester semester = semesterRepository.findById(dto.getSemesterCode())
                    .orElseThrow(() -> new EntityNotFoundException("not found"));
            if (!(user.getMajor() == semester.getMajor())){
                throw new RuntimeException("This course is not in teacher major to take!");
            }
            entity.setTeacher(user);
        }
        entity.setACourseStartDate(dto.getACourseStartDate());
        entity.setACourseEndDate(dto.getACourseEndDate());
    }

    @AfterMapping
    protected void afterToDTO(AvailableCourse entity, @MappingTarget AvailableCourseDto dto) {
        if (entity.getSemester() != null) {
            dto.setSemesterCode(entity.getSemester().getId());
        }
        if (entity.getCourse() != null) {
            dto.setCourseCode(entity.getCourse().getId());
        }
        if (entity.getTeacher() != null) {
            dto.setTeacherId(entity.getTeacher().getId());
        }
    }
}
