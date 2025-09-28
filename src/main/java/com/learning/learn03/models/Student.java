package com.learning.learn03.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.*;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student extends User {
        @ManyToMany
        private List<Course> course = new ArrayList<>();
}