package com.learning.learn03.interfaces;

import com.learning.learn03.dtos.StudentDto;
import com.learning.learn03.dtos.TeacherDto;
import com.learning.learn03.models.User;

import java.util.List;

public interface IUserService {
    public User login(String email, String password);

    public void registerStudent(StudentDto studentDto);

    public void registerTeacher(TeacherDto teacherDto);

    public List<User> findAll();
}
