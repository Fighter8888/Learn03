package com.learning.learn03.controllers;

import com.learning.learn03.dtos.RequestDTO;
import com.learning.learn03.dtos.ResponseDTO;
import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.dtos.UserMapper;
import com.learning.learn03.models.User;
import com.learning.learn03.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }


    @PostMapping("/student/register")
    public ResponseEntity<ApiResponseDTO> studentRegister(@RequestBody UserDto request) {
        User user = authenticationService.persist(userMapper.toEntity(request));
        authenticationService.addRoleToPerson("student" , user.getId());
        ApiResponseDTO responseDTO = new ApiResponseDTO("Register success" , true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody RequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request));
    }



}
