package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.CourseDto;
import com.learning.learn03.models.Course;
import com.learning.learn03.models.Major;
import com.learning.learn03.repositories.MajorRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CourseMapper implements BaseMapper<Course, CourseDto> {

    @Autowired
    private MajorRepository majorRepository;

    public abstract CourseDto toDto(Course entity);

    public abstract Course toEntity(CourseDto dto);


    @AfterMapping
    protected void afterToEntity(CourseDto dto, @MappingTarget Course course) {
        if (dto.getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(dto.getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major with name " + dto.getMajorName() + " not found"
                    ));
            course.setMajor(major);
        }
    }

    @AfterMapping
    protected void afterToDTO(Course course, @MappingTarget CourseDto dto) {
        if (course.getMajor().getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(course.getMajor().getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major with name " + dto.getMajorName() + " not found"
                    ));
            dto.setMajorName(major.getMajorName());
        }
    }

}
