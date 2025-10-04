package com.learning.learn03.controllers;

import com.learning.learn03.dtos.RegisterDTO;
import com.learning.learn03.dtos.RegisterMapper;
import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.interfaces.IAuthenticationService;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import com.learning.learn03.services.AuthenticationService;
import com.learning.learn03.services.PrincipalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/principal")
public class PrincipalController {



    private final IAuthenticationService iAuthenticationService;
    private final RegisterMapper registerMapper;

    public PrincipalController(IAuthenticationService iAuthenticationService, RegisterMapper registerMapper) {
        this.iAuthenticationService = iAuthenticationService;
        this.registerMapper = registerMapper;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/teacher/register")
    public ResponseEntity<ApiResponseDTO> teacherRegister(@RequestBody RegisterDTO request) {
        User user = iAuthenticationService.persist(registerMapper.toEntity(request));
        iAuthenticationService.addRoleToPerson("teacher" , user.getId());
        ApiResponseDTO responseDTO = new ApiResponseDTO("Register success" , true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/manager/register")
    public ResponseEntity<ApiResponseDTO> managerRegister(@RequestBody RegisterDTO request) {
        User user = iAuthenticationService.persist(registerMapper.toEntity(request));
        iAuthenticationService.addRoleToPerson("manager" , user.getId());
        ApiResponseDTO responseDTO = new ApiResponseDTO("Register success" , true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/role")
    public ResponseEntity<ApiResponseDTO> addRoleToPerson(@RequestBody AddRoleRequest request) {
        iAuthenticationService.addRoleToPerson(request.getRole() , request.getPersonId());
        return ResponseEntity.ok(new ApiResponseDTO("Add role success", true));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/active/{id}")
    public ResponseEntity<ApiResponseDTO> activeAccount(@PathVariable Integer id) {
        iAuthenticationService.activeAccount(id);
        String msg = "user active successfully.";
        return ResponseEntity.ok(new ApiResponseDTO(msg , true));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/inactive/{id}")
    public ResponseEntity<ApiResponseDTO> inactiveAccount(@PathVariable Integer id) {
        iAuthenticationService.inactiveAccount(id);
        String msg = "user inactive successfully.";
        return ResponseEntity.ok(new ApiResponseDTO(msg , true));
    }

//    private final PrincipalService principalService;
//
//    public PrincipalController(PrincipalService principalService) {
//        this.principalService = principalService;
//    }
//
//    @GetMapping("/pending")
//    public ResponseEntity<List<UserDto>> getPendingUsers() {
//        return ResponseEntity.ok(principalService.getPendingUsers());
//    }
//
//    @PutMapping("/{id}/status")
//    public ResponseEntity<UserDto> updateUserStatus(@PathVariable int id, @RequestParam UserStatus status) {
//        return ResponseEntity.ok(principalService.updateUserStatus(id, status));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @RequestBody UserDto updatedUser) {
//        return ResponseEntity.ok(principalService.updateUser(id, updatedUser));
//    }
}