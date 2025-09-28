package com.learning.learn03.repositories;

import com.learning.learn03.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
}
