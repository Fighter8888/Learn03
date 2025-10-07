package com.learning.learn03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.learn03.dtos.AuthenticationRequestDto;
import com.learning.learn03.dtos.AuthenticationResponse;
import com.learning.learn03.dtos.AvailableCourseDto;
import com.learning.learn03.models.*;
import com.learning.learn03.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailableCourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private AvailableCourseRepository availableCourseRepository;

    private String accessToken;

    @BeforeEach
    void beforeEach() throws Exception {
        availableCourseRepository.deleteAll();
        courseRepository.deleteAll();
        semesterRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        this.accessToken = loginAsPrincipal();
    }

    @Test
    void createAvailableCourse() throws Exception {
        Major major = getMajor("MADNESS");
        Course course = createCourse("cc01", major);
        User teacher = createTeacher(major);
        Semester semester = createSemester(major, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        AvailableCourseDto dto = AvailableCourseDto.builder()
                .aCourseStartDate(Instant.parse("2025-11-23T11:00:00Z"))
                .aCourseEndDate(Instant.parse("2025-11-23T12:00:00Z"))
                .capacity(1)
                .courseCode(course.getId())
                .teacherId(teacher.getId())
                .semesterCode(semester.getId())
                .build();

        objectMapper.registerModule(new JavaTimeModule());
        // Write dates as ISO strings instead of timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/web/availableCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    void updateAvailableCourse() throws Exception {
        Major major = getMajor("MADNESS");
        Course course = createCourse("cc20", major);
        User teacher = createTeacher(major);
        Semester semester = createSemester(major, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));
        AvailableCourse availableCourse = createAvailableCourse(course, semester);

        AvailableCourseDto dto = AvailableCourseDto.builder()
                .aCourseStartDate(Instant.parse("2026-11-23T11:00:00Z"))
                .aCourseEndDate(Instant.parse("2026-11-23T12:00:00Z"))
                .capacity(69)
                .courseCode(course.getId())
                .teacherId(teacher.getId())
                .semesterCode(semester.getId())
                .build();

        mockMvc.perform(put("/web/availableCourse/" + availableCourse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAvailableCourse() throws Exception {
        Major major = getMajor("MADNESS");
        Course course = createCourse("course11", major);
        Semester semester = createSemester(major, LocalDate.of(2016, 6, 9), LocalDate.of(2025, 11, 20));
        AvailableCourse availableCourse = createAvailableCourse(course, semester);

        mockMvc.perform(delete("/web/availableCourse/" + availableCourse.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        Major major = getMajor("MADNESS");
        Course course = createCourse("course12", major);
        Semester semester = createSemester(major, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));
        AvailableCourse availableCourse = createAvailableCourse(course, semester);

        mockMvc.perform(get("/web/availableCourse/" + availableCourse.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/web/availableCourse")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    //========================================================================================//
    private String loginAsPrincipal() throws Exception {
        Role role = roleRepository.findByRoleName("PRINCIPAL").orElseThrow();

        String uniqueEmail = "principal" + System.currentTimeMillis() + "@gmail.com";

        User principal = createUser("PRINCIPAL", "PRINCIPAL", uniqueEmail, role, null);
        Account account = createAccount(principal, role);
        principal.setAccount(account);
        accountRepository.save(account);

        return loginAndGetToken(principal.getEmail(), principal.getPhoneNumber());
    }

    private User createUser(String firstName, String lastName, String email, Role role, Major major) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(randomPhone())
                .email(email)
                .roles(new ArrayList<>(List.of(role)))
                .major(major)
                .build();
        return userRepository.save(user);
    }

    private Account createAccount(User user, Role role) {
        Account account = Account.builder()
                .userName(user.getEmail())
                .password(passwordEncoder.encode(user.getPhoneNumber()))
                .status(UserStatus.Approved)
                .user(user)
                .role(role)
                .build();
        user.setAccount(account);
        return accountRepository.save(account);
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        AuthenticationRequestDto auth = AuthenticationRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        String jwtToken = mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(jwtToken, AuthenticationResponse.class).getAccessToken();
    }

    private Major getMajor(String majorName) {
        return majorRepository.findByMajorName(majorName).orElseThrow();
    }

    private Course createCourse(String title, Major major) {
        Course course = Course.builder().courseName(title).major(major).build();
        return courseRepository.save(course);
    }

    private Semester createSemester(Major major, LocalDate start, LocalDate end) {
        Semester semester = Semester.builder().semesterStartDate(start).semesterEndDate(end).major(major).build();
        return semesterRepository.save(semester);
    }

    private AvailableCourse createAvailableCourse(Course course, Semester semester) {
        AvailableCourse availableCourse = AvailableCourse.builder()
                .aCourseStartDate(Instant.parse("2025-11-23T11:00:00Z"))
                .aCourseEndDate(Instant.parse("2025-11-23T12:00:00Z"))
                .semester(semester)
                .course(course)
                .courseStatus(CourseStatus.UNFILLED)
                .capacity(69)
                .build();
        return availableCourseRepository.save(availableCourse);
    }

    private User createTeacher(Major major) {
        Role role = roleRepository.findByRoleName("TEACHER").orElseThrow();
        User teacher = createUser("Teacher", "Teacher", "teacher" + System.nanoTime() + "@gmail.com", role, major);
        createAccount(teacher, role);
        return teacher;
    }

    private static String randomPhone() {
        StringBuilder sb = new StringBuilder("09");
        for (int i = 0; i < 9; i++) sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        return sb.toString();
    }
}
