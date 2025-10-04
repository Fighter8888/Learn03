package com.learning.learn03.services;

import com.learning.learn03.base.BaseService;
import com.learning.learn03.interfaces.IAvailableCourseService;
import com.learning.learn03.models.*;
import com.learning.learn03.repositories.AccountRepository;
import com.learning.learn03.repositories.AvailableCourseRepository;
import com.learning.learn03.repositories.SemesterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class AvailableCourseService extends BaseService<AvailableCourse, Integer> implements IAvailableCourseService {

    private final AccountRepository accountRepository;
    private final AvailableCourseRepository  availableCourseRepository;
    private final SemesterRepository semesterRepository;
    
    protected AvailableCourseService(JpaRepository<AvailableCourse, Integer> repository, AccountRepository accountRepository, AvailableCourseRepository availableCourseRepository, SemesterRepository semesterRepository) {
        super(repository);
        this.accountRepository = accountRepository;
        this.availableCourseRepository = availableCourseRepository;
        this.semesterRepository = semesterRepository;
    }

    @Override
    protected void prePersist(AvailableCourse offeredCourse) {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.toInstant(ZoneOffset.UTC);
        if (offeredCourse.getACourseStartDate() == null || offeredCourse.getACourseEndDate() == null) {
            throw new IllegalArgumentException("Offered course start and end times must not be null");
        }

        if (!offeredCourse.getACourseStartDate().isAfter(instant)) {
            throw new IllegalArgumentException("Offered course start time must be in the future");
        }

        if (!offeredCourse.getACourseEndDate().isAfter(instant)) {
            throw new IllegalArgumentException("Offered course end time must be in the future");
        }

        if (!offeredCourse.getACourseStartDate().isBefore(offeredCourse.getACourseEndDate())) {
            throw new IllegalArgumentException("Offered course start time must be before end time");
        }
        offeredCourse.setCourseStatus(CourseStatus.UNFILLED);
    }

    @Override
    protected void preUpdate(AvailableCourse offeredCourse) {
        LocalDate termStartDate = offeredCourse.getSemester().getSemesterStartDate();
        if (!termStartDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Can't update Offered course after term start date!");
        }
    }


    @Override
    public List<AvailableCourse> findAllTeacherCourse(Principal principal) {
        String username = principal.getName();
        Account account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        User user = account.getUser();
        return user.getAvailableCourses();
    }


    @Override
    public List<AvailableCourse> findAllStudentCourses(Principal principal) {
        Account account = accountRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        User user = account.getUser();

        return user.getAvailableCourses();
    }


    @Override
    public List<AvailableCourse> findAllTermCourses(int semesterId, Principal principal) {
        Account account = accountRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new EntityNotFoundException("Semester not found"));

        if (!account.getRole().getRoleName().equals("ADMIN")) {
            if (!account.getUser().getMajor().getMajorName().equals(semester.getMajor().getMajorName())) {
                throw new AccessDeniedException("You are not allowed to access this semester!");
            }
        }
        return semester.getAvailableCourses();
    }
}
