package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Semester extends BaseEntity<Integer> {

    private Instant semesterStartDate;
    private Instant semesterEndDate;
    public boolean semesterExist;

    @OneToMany(mappedBy = "semester")
    private List<AvailableCourse> availableCourses = new ArrayList<>();

    @ManyToOne
    private Major major;
}
