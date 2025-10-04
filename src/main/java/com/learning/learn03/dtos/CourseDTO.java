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
public class CourseDTO {
    @NotBlank
    private int courseCode;
    @NotBlank
    private String title;
    @NotBlank
    private String majorName;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank
    private int capacity;
}
