package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends BaseEntity<Integer> {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private int courseCode;
    private String courseName;
    private boolean courseExist;

    @ManyToOne
    private Major major;

    @OneToMany
    private List<AvailableCourse> availableCourses =  new ArrayList<>();
}
