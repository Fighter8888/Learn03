package com.learning.learn03.controllers;


import com.learning.learn03.services.SemesterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/semester")
public class SemesterController {
    private final SemesterService semesterService;
    private final TermMapper termMapper;

    public SemesterController(SemesterService semesterService, TermMapper termMapper) {
        this.semesterService = semesterService;
        this.termMapper = termMapper;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TermDTO> save(@RequestBody TermDTO termDTO) {
        Term persist = termService.persist(termMapper.toEntity(termDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(termMapper.toDto(persist));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TermDTO> update(@PathVariable Long id, @RequestBody TermDTO dto) {
        Term foundedTerm = termService.findById(id);
        foundedTerm.setStartDate(dto.getStartDate());
        foundedTerm.setEndDate(dto.getEndDate());
        Term term = termService.persist(foundedTerm);
        return ResponseEntity.ok(termMapper.toDto(term));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable Long id) {
        termService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO("Term deleted success." , true));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(termMapper.toDto(termService.findById(id)));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TermDTO>> findAll() {
        List<TermDTO> termDTOS = new ArrayList<>();
        for (Term term : termService.findAll()) termDTOS.add(termMapper.toDto(term));
        return ResponseEntity.ok(termDTOS);
    }
}
