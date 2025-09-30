package com.learning.learn03.services;

import com.learning.learn03.dtos.*;
import com.learning.learn03.interfaces.IUserService;
import com.learning.learn03.models.*;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.StudentRepository;
import com.learning.learn03.repositories.TeacherRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ch.qos.logback.core.util.AggregationType.NOT_FOUND;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse login(LoginDto loginDto) {
        // Authenticate the user
        final Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        final UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Find the user entity
        final User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        // Generate JWT token
        final String accessToken = jwtService.generateAccessToken(user.getEmail());
        final String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // Return response
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    public void registerStudent(StudentDto dto) {
        Role studentRole = roleRepository.findByName("STUDENT").get();
        studentRepository.save(Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .roles(Collections.singletonList(studentRole))
                .status(UserStatus.Pending)
                .build());
    }

    public void registerTeacher(TeacherDto dto) {
        Role teacherRole = roleRepository.findByName("TEACHER").get();
        teacherRepository.save(Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .roles(Collections.singletonList(teacherRole))
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
