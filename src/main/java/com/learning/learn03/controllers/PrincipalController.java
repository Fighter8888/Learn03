package com.learning.learn03.controllers;

import com.learning.learn03.dtos.AddRoleRequest;
import com.learning.learn03.dtos.ApiResponseDto;
import com.learning.learn03.dtos.RegisterDto;
import com.learning.learn03.mappers.RegisterMapper;
import com.learning.learn03.interfaces.IAuthenticationService;
import com.learning.learn03.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponseDto> teacherRegister(@RequestBody RegisterDto request) {
        User user = iAuthenticationService.persist(registerMapper.toEntity(request));
        iAuthenticationService.addRoleToPerson("teacher" , user.getId());
        ApiResponseDto responseDTO = new ApiResponseDto("Register success" , true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/manager/register")
    public ResponseEntity<ApiResponseDto> managerRegister(@RequestBody RegisterDto request) {
        User user = iAuthenticationService.persist(registerMapper.toEntity(request));
        iAuthenticationService.addRoleToPerson("manager" , user.getId());
        ApiResponseDto responseDTO = new ApiResponseDto("Register success" , true);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/role")
    public ResponseEntity<ApiResponseDto> addRoleToPerson(@RequestBody AddRoleRequest request) {
        iAuthenticationService.addRoleToPerson(request.getRole() , request.getPersonId());
        return ResponseEntity.ok(new ApiResponseDto("Add role success", true));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/active/{id}")
    public ResponseEntity<ApiResponseDto> activeAccount(@PathVariable Integer id) {
        iAuthenticationService.activeAccount(id);
        String msg = "user active successfully.";
        return ResponseEntity.ok(new ApiResponseDto(msg , true));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/inactive/{id}")
    public ResponseEntity<ApiResponseDto> inactiveAccount(@PathVariable Integer id) {
        iAuthenticationService.inactiveAccount(id);
        String msg = "user inactive successfully.";
        return ResponseEntity.ok(new ApiResponseDto(msg , true));
    }

}