package com.learning.learn03.controller;

import com.learning.learn03.model.User;
import com.learning.learn03.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/student/register")
    public ResponseEntity<User> registerStudent(@RequestBody User student) {
        userService.register(student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping("/teacher/register")
    public ResponseEntity<User> registerTeacher(@RequestBody User teacher) {
        userService.register(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(User user) {
        userService.login(user.getFirstName(), user.getLastName());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }
}
