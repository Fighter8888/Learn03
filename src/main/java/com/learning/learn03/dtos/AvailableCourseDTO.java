package com.learning.learn03.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCourseDTO {
    private Instant startTime;
    private Instant endTime;
    private int capacity;
    private Integer courseCode;
    private Integer semesterCode;
    private Integer teacherId;
}
