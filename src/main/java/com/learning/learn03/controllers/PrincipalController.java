package com.learning.learn03.controllers;

import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import com.learning.learn03.services.PrincipalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/principal")
public class PrincipalController {
    private final PrincipalService principalService;

    public PrincipalController(PrincipalService principalService) {
        this.principalService = principalService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<User>> getPendingUsers() {
        return ResponseEntity.ok(principalService.getPendingUsers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable int id, @RequestParam UserStatus status) {
        return ResponseEntity.ok(principalService.updateUserStatus(id, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(principalService.updateUser(id, updatedUser));
    }
}
