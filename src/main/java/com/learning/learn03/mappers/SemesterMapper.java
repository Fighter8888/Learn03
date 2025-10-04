package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.SemesterDTO;
import com.learning.learn03.models.Major;
import com.learning.learn03.models.Semester;
import com.learning.learn03.repositories.MajorRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SemesterMapper implements BaseMapper<Semester, SemesterDTO> {

    @Autowired
    private MajorRepository majorRepository;

    public abstract SemesterDTO toDto(Semester entity);

    public abstract Semester toEntity(SemesterDTO dto);

    @AfterMapping
    protected void afterToEntity(SemesterDTO dto, @MappingTarget Semester semester) {
        if (dto.getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(dto.getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major with name " + dto.getMajorName() + " not found"
                    ));
            semester.setMajor(major);
        }
    }

    @AfterMapping
    protected void afterToDTO(Semester semester, @MappingTarget SemesterDTO dto) {
        if (semester.getMajor().getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(semester.getMajor().getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major with name " + dto.getMajorName() + " not found"
                    ));
            dto.setMajorName(major.getMajorName());
        }
    }
}
