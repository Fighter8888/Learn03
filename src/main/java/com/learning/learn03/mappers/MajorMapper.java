package com.learning.learn03.mappers;

import com.learning.learn03.base.BaseMapper;
import com.learning.learn03.dtos.MajorDto;
import com.learning.learn03.models.Major;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper extends BaseMapper<Major, MajorDto> {
}
