package com.learning.learn03.services.Implements;

import com.learning.learn03.base.BaseService;
import com.learning.learn03.services.ISemesterService;
import com.learning.learn03.models.Semester;
import com.learning.learn03.repositories.SemesterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterServiceImpl extends BaseService<Semester, Integer> implements ISemesterService {
    
    private final SemesterRepository semesterRepository;
    protected SemesterServiceImpl(JpaRepository<Semester, Integer> repository, SemesterRepository semesterRepository) {
        super(repository);
        this.semesterRepository = semesterRepository;
    }


    @Override
    protected void prePersist(Semester semester) {
        LocalDate now = LocalDate.now();
        if (semester.getSemesterStartDate() == null || semester.getSemesterEndDate() == null) {
            throw new IllegalArgumentException("Semester start and end times must not be null");
        }

        if (!semester.getSemesterStartDate().isAfter(now)) {
            throw new IllegalArgumentException("Semester start time must be in the future");
        }

        if (!semester.getSemesterEndDate().isAfter(now)) {
            throw new IllegalArgumentException("Semester end time must be in the future");
        }

        if (!semester.getSemesterStartDate().isBefore(semester.getSemesterEndDate())) {
            throw new IllegalArgumentException("Semester start time must be before end time");
        }
        semester.setSemesterActive(true);
    }

    @Override
    protected void preUpdate(Semester semester) {
        LocalDate now = LocalDate.now();
        if (!semester.getSemesterStartDate().isAfter(now)) {
            throw new AccessDeniedException("Can't update Semester after starting date!");
        }
    }

    @Override
    public void delete(Integer id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("semester not found!"));
        LocalDate now = LocalDate.now();
        if (!semester.getSemesterStartDate().isAfter(now)) {
            throw new AccessDeniedException("Can't delete term after start date!");
        }
        semester.setSemesterActive(false);
    }

    @Override
    public Semester findById(Integer id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Semester not found"));
        if (!semester.isSemesterActive()) {
            throw new EntityNotFoundException("Semester not found");
        }
        return semester;
    }



    @Override
    public List<Semester> findAll() {
        List<Semester> semesters = semesterRepository.findAll();
        List<Semester> result = new ArrayList<>();
        for (Semester semester : semesters) {
            if (semester.isSemesterActive()) {
                result.add(semester);
            }
        }
        return result;
    }
}
