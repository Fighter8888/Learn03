package com.learning.learn03.repository;

import com.learning.learn03.model.Teacher;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepositoryImplementation<Teacher,Integer> {
    List<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
}
