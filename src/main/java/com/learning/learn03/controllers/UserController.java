package com.learning.learn03.controllers;

import com.learning.learn03.dtos.ApiResponseDto;
import com.learning.learn03.models.Role;
import com.learning.learn03.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STUDENT') or hasRole('TEACHER') or hasRole('USER')")
    @PostMapping("/change/role")
    public ResponseEntity<ApiResponseDto> changeRole(@RequestBody Role request , Principal principal) {
        authenticationService.changeRole(principal.getName(), request.getRoleName());
        return ResponseEntity.ok(new ApiResponseDto("Change role success", true));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STUDENT') or hasRole('TEACHER') or hasRole('USER')")
    @GetMapping("/get/roles")
    public ResponseEntity<List<String>> getRoles(Principal principal) {
        return ResponseEntity.ok(authenticationService.getPersonRoles(principal).stream().map(Role::getRoleName).toList());
    }
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/student/register")
//    public ResponseEntity<Teacher> registerStudent(@RequestBody StudentDto student) {
//        userService.registerStudent(student);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    @PostMapping("/teacher/register")
//    public ResponseEntity<Teacher> registerTeacher(@RequestBody TeacherDto teacher) {
//        userService.registerTeacher(teacher);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    //    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody LoginDto loginDto) {
//        User loggedInUser = userService.login(loginDto.getEmail(), loginDto.getPassword());
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loggedInUser);
//    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody  LoginDto loginDto) {
//        AuthenticationResponse login = userService.login(loginDto);
//        return new ResponseEntity<>(login, HttpStatus.OK);
//    }
//
//    @PreAuthorize("hasRole('Principal')")
//    @PostMapping("/change/role")
//    public ResponseEntity<UserDto> changeRole(@RequestBody UserDto user, RoleDto roleDto) {
//        userService.changeRole(user.getEmail(), roleDto.getName());
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
//    }
}