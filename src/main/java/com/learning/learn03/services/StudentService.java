package com.learning.learn03.services;

import com.learning.learn03.interfaces.IStudentService;
import com.learning.learn03.models.Account;
import com.learning.learn03.models.AvailableCourse;
import com.learning.learn03.models.User;
import com.learning.learn03.repositories.AccountRepository;
import com.learning.learn03.repositories.AvailableCourseRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class StudentService implements IStudentService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AvailableCourseRepository availableCourseRepository;

    public StudentService(UserRepository userRepository, AccountRepository accountRepository, AvailableCourseRepository availableCourseRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.availableCourseRepository = availableCourseRepository;
    }

    @Override
    public void CourseRequest(int courseId, Principal principal) {
        Account account = accountRepository.findByUserName(principal.getName()) //Problem in here
                .orElseThrow(()->  new EntityNotFoundException(String.format("%s not found!", "Account")));

        User user = account.getUser();

        AvailableCourse availableCourse = availableCourseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("%s not found!", "Course")));

        if (!(availableCourse.getSemester().getMajor() == user.getMajor())) {
            throw new AccessDeniedException("Don't have enough permissions to access this course!");
        }

        availableCourse.getStudents().add(user);
        availableCourseRepository.save(availableCourse);


        user.getAvailableCourses().add(availableCourse);
        userRepository.save(user);

    }
}
