package com.learning.learn03.controllers;

import com.learning.learn03.config.CustomUserDetailsService;
import com.learning.learn03.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(List.of(studentRole));
//        user.setStatus(UserStatus.Pending);
//        userRepository.save(user);

//        return ResponseEntity.ok("User registered successfully. Waiting for admin approval.");
//    }
}