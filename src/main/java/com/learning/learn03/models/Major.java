package com.learning.learn03.models;

import com.learning.learn03.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Major extends BaseEntity<Integer> {

    @NotBlank
    private String majorName;
    private boolean majorActive;
    @NotNull
    private UUID majorCode;

    @OneToMany
    private List<Semester> semesters = new ArrayList<>();
    @OneToMany
    private List<User> users = new ArrayList<>();
    @OneToMany
    private List<Course> courses = new ArrayList<>();

}
