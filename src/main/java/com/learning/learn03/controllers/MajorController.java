package com.learning.learn03.controllers;

import com.learning.learn03.dtos.MajorDTO;
import com.learning.learn03.interfaces.IMajorService;
import com.learning.learn03.mappers.MajorMapper;
import com.learning.learn03.models.Major;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/web/major")
public class MajorController {


    private final IMajorService iMajorService;
    private final MajorMapper majorMapper;

    public MajorController(IMajorService iMajorService, MajorMapper majorMapper) {
        this.iMajorService = iMajorService;
        this.majorMapper = majorMapper;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MajorDTO> save(@RequestBody MajorDTO dto) {
        Major major = iMajorService.persist(majorMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(majorMapper.toDto(major));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{majorId}")
    public ResponseEntity<MajorDTO> update(@PathVariable int majorId, @RequestBody MajorDTO dto) {
        Major foundedMajor = iMajorService.findById(majorId);
        foundedMajor.setMajorName(dto.getMajorName());
        Major major = iMajorService.persist(foundedMajor);
        return ResponseEntity.ok(majorMapper.toDto(major));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable int id) {
        iMajorService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDTO("Major deleted success.", true));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MajorDTO> findById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(majorMapper.toDto(iMajorService.findById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MajorDTO>> findAll() {
        List<MajorDTO> majors = new ArrayList<>();
        for (Major major : iMajorService.findAll()) {
            majors.add(majorMapper.toDto(major));
        }
        return ResponseEntity.status(HttpStatus.OK).body(majors);
    }
}

