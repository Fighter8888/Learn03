package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.AvailableCourseResponseDto;
import com.learning.learn03.models.AvailableCourse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AvailableCourseResponseMapper implements BaseMapper<AvailableCourse, AvailableCourseResponseDto> {

    public abstract AvailableCourseResponseDto toDto(AvailableCourse entity);

    public abstract AvailableCourse toEntity(AvailableCourseResponseDto dto);


    @AfterMapping
    protected void afterToDTO(AvailableCourse entity, @MappingTarget AvailableCourseResponseDto dto) {
        if (entity.getSemester() != null) {
            dto.setSemesterCode(entity.getSemester().getId());
        }
        if (entity.getCourse() != null) {
            dto.setCourseName(entity.getCourse().getCourseName());
        }
        if (entity.getTeacher() != null) {
            dto.setTeacherName(entity.getTeacher().getFirstName() + " " + entity.getTeacher().getLastName());
        }
        if (entity.getSemester() != null) {
            dto.setMajorName(entity.getSemester().getMajor().getMajorName());
        }
    }
}
