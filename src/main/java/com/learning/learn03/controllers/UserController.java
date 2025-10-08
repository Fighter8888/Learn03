package com.learning.learn03.controllers;

import com.learning.learn03.dtos.ApiResponseDto;
import com.learning.learn03.models.Role;
import com.learning.learn03.services.Implements.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    public UserController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'STUDENT', 'TEACHER', 'USER')")
    @PostMapping("/changerole")
    public ResponseEntity<ApiResponseDto> changeRole(@RequestBody Role request, Principal principal) {
        authenticationServiceImpl.changeRole(principal.getName(), request.getRoleName());
        return ResponseEntity.ok(new ApiResponseDto("Change role success", true));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'STUDENT', 'TEACHER', 'USER')")
    @GetMapping("/getroles")
    public ResponseEntity<List<String>> getRoles(Principal principal) {
        return ResponseEntity.ok(authenticationServiceImpl.getUserRoles(principal).stream().map(Role::getRoleName).toList());
    }
}