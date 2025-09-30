package com.learning.learn03.interfaces;

import com.learning.learn03.dtos.*;
import com.learning.learn03.models.User;

import java.util.List;

public interface IUserService {
    public AuthenticationResponse login(LoginDto loginDto);

    public void registerStudent(StudentDto studentDto);

    public void registerTeacher(TeacherDto teacherDto);

    public List<User> findAll();
}
