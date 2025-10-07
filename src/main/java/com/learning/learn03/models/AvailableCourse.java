package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AvailableCourse extends BaseEntity<Integer> {
    private int capacity;
    private Instant aCourseStartDate;
    private Instant aCourseEndDate;

    private CourseStatus courseStatus;
    @ManyToOne
    private Course course;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private User teacher;

    @ManyToMany
    private List<User> students = new ArrayList<>();
}
