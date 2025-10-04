package com.learning.learn03.controllers;


import com.learning.learn03.dtos.SemesterDTO;
import com.learning.learn03.interfaces.ISemesterService;
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
    public ResponseEntity<SemesterDTO> save(@RequestBody SemesterDTO termDTO) {
        Semester persist = iSemesterService.persist(semesterMapper.toEntity(termDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(semesterMapper.toDto(persist));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SemesterDTO> update(@PathVariable int id, @RequestBody SemesterDTO dto) {
        Semester foundedSemester = iSemesterService.findById(id);
        foundedSemester.setSemesterStartDate(dto.getStartDate());
        foundedSemester.setSemesterEndDate(dto.getEndDate());
        Semester semester = iSemesterService.persist(foundedSemester);
        return ResponseEntity.ok(semesterMapper.toDto(semester));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable int id) {
        iSemesterService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO("Semester deleted success." , true));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SemesterDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(semesterMapper.toDto(iSemesterService.findById(id)));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SemesterDTO>> findAll() {
        List<SemesterDTO> semesterDTOS = new ArrayList<>();
        for (Semester semester : iSemesterService.findAll()) semesterDTOS.add(semesterMapper.toDto(semester));
        return ResponseEntity.ok(semesterDTOS);
    }
}
