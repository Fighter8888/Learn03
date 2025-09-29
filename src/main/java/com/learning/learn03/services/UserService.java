package com.learning.learn03.services;

import com.learning.learn03.dtos.StudentDto;
import com.learning.learn03.dtos.TeacherDto;
import com.learning.learn03.interfaces.IUserService;
import com.learning.learn03.models.*;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.StudentRepository;
import com.learning.learn03.repositories.TeacherRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

import static ch.qos.logback.core.util.AggregationType.NOT_FOUND;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .filter(u -> u.getStatus() == UserStatus.Approved)
                .orElseThrow(() -> new RuntimeException("Invalid credentials or user not approved"));
    }

    public void registerStudent(StudentDto dto) {
        Role studentRole = roleRepository.findByName("STUDENT").get();
        studentRepository.save(Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles((List<Role>) studentRole)
                .status(UserStatus.Pending)
                .build());
    }

    public void registerTeacher(TeacherDto dto) {
        Role teacherRole = roleRepository.findByName("TEACHER").get();
        teacherRepository.save(Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles((List<Role>) teacherRole)
                .status(UserStatus.Pending)
                .build());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void changeRole(String email , String roleName) {
        User account = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format( "Account")));

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(String.format( "Role")));

        if (account.getRoles().contains(role)) {
            account.setRoles((List<Role>) role);
            userRepository.save(account);
        }
        else throw new AccessDeniedException("Can't change this role!");
    }
}
