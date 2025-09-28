package com.learning.learn03.models;

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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int code;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;

    @ManyToMany
    private List<Student> students = new ArrayList<>();
    @ManyToOne
    private Teacher teacher;
}
