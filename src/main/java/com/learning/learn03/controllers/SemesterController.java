package com.learning.learn03.controllers;


import com.learning.learn03.dtos.ApiResponseDto;
import com.learning.learn03.dtos.SemesterDto;
import com.learning.learn03.services.ISemesterService;
import com.learning.learn03.mappers.SemesterMapper;
import com.learning.learn03.models.Semester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/web/semester")
public class SemesterController{
    
    private final ISemesterService iSemesterService;
    private final SemesterMapper semesterMapper;

    public SemesterController(ISemesterService iSemesterService, SemesterMapper semesterMapper) {
        this.iSemesterService = iSemesterService;
        this.semesterMapper = semesterMapper;
    }


    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SemesterDto> save(@RequestBody SemesterDto termDTO) {
        Semester persist = iSemesterService.persist(semesterMapper.toEntity(termDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(semesterMapper.toDto(persist));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SemesterDto> update(@PathVariable int id, @RequestBody SemesterDto dto) {
        Semester foundedSemester = iSemesterService.findById(id);
        foundedSemester.setSemesterStartDate(dto.getStartDate());
        foundedSemester.setSemesterEndDate(dto.getEndDate());
        Semester semester = iSemesterService.persist(foundedSemester);
        return ResponseEntity.ok(semesterMapper.toDto(semester));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable int id) {
        iSemesterService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("Semester deleted success." , true));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SemesterDto> findById(@PathVariable int id) {
        return ResponseEntity.ok(semesterMapper.toDto(iSemesterService.findById(id)));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SemesterDto>> findAll() {
        List<SemesterDto> semesterDtos = new ArrayList<>();
        for (Semester semester : iSemesterService.findAll()) semesterDtos.add(semesterMapper.toDto(semester));
        return ResponseEntity.ok(semesterDtos);
    }
}
