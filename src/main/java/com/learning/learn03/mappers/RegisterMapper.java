package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.RegisterDto;
import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.models.Major;
import com.learning.learn03.models.User;
import com.learning.learn03.repositories.MajorRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RegisterMapper implements BaseMapper<User, RegisterDto> {

    @Autowired
    private MajorRepository majorRepository;

    public abstract RegisterDto toDto(User entity);

    public abstract User toEntity(UserDto dto);

    @AfterMapping
    protected void afterToEntity(RegisterDto dto, @MappingTarget User user) {
        if (dto.getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(dto.getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major not found"
                    ));
            user.setMajor(major);
        }
    }

    @AfterMapping
    protected void afterToDTO(User user, @MappingTarget RegisterDto dto) {
        if (user.getMajor().getMajorName() != null) {
            Major major = majorRepository
                    .findByMajorName(user.getMajor().getMajorName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Major not found"
                    ));
            dto.setMajorName(major.getMajorName());
        }
    }
}