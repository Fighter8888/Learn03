package com.learning.learn03.dtos;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCourseResponseDto {
    private Instant startTime;
    private Instant endTime;
    private Integer capacity;
    private String courseName;
    private String teacherName;
    private String majorName;
    private Integer semesterCode;
}
