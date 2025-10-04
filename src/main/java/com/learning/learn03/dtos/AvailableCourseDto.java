package com.learning.learn03.dtos;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCourseDto {
    private Instant startTime;
    private Instant endTime;
    private int capacity;
    private Integer courseCode;
    private Integer semesterCode;
    private Integer teacherId;
}
