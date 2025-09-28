package com.learning.learn03.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    @NotBlank
    private int code;
    @NotBlank
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank
    private int capacity;
}
