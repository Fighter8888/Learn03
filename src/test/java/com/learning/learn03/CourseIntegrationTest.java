//package com.learning.learn03;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.learning.learn03.dtos.AuthenticationRequestDto;
//import com.learning.learn03.dtos.AuthenticationResponse;
//import com.learning.learn03.dtos.CourseDto;
//import com.learning.learn03.models.*;
//import com.learning.learn03.repositories.*;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ThreadLocalRandom;
//
//import static org.hamcrest.Matchers.greaterThan;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class CourseIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private CourseRepository courseRepository;
//    @Autowired
//    private MajorRepository majorRepository;
//
//    private String accessToken;
//
//    @BeforeAll
//    void setupAdmin() throws Exception {
//        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow();
//        User admin = createPerson("Admin", "Admin", adminRole);
//        createAccount(admin, adminRole);
//        this.accessToken = loginAndGetToken(admin.getPhoneNumber(), admin.getPhoneNumber());
//    }
//
//    @Test
//    void saveCourse() throws Exception {
//        Major major = createMajor("Computer" + UUID.randomUUID());
//
//        CourseDto dto = CourseDto.builder()
//                .title("course1")
//                .majorName(major.getMajorName())
//                .build();
//
//        mockMvc.perform(post("/web/course")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto))
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.title").value(dto.getTitle()));
//    }
//
//    @Test
//    void updateCourse() throws Exception {
//        Major major = createMajor("Accounting");
//        Course course = createCourse("Old Title", "Old Desc", major);
//
//        CourseDto dto = CourseDto.builder()
//                .title("New Title")
//                .majorName(major.getMajorName())
//                .build();
//
//        mockMvc.perform(put("/web/course/" + course.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto))
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("New Title"));
//    }
//
//    @Test
//    void deleteCourse() throws Exception {
//        Major major = createMajor("English");
//        Course course = createCourse("To Delete", "Desc", major);
//
//        mockMvc.perform(delete("/web/course/" + course.getId())
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk());
//
//        Assertions.assertTrue(courseRepository.findById(course.getId()).get().isDeleted());
//    }
//
//    @Test
//    void findCourseById() throws Exception {
//        Major major = createMajor("IT");
//        Course course = createCourse("Find Me", "Desc", major);
//
//        mockMvc.perform(get("/web/course/" + course.getId())
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Find Me"));
//    }
//
//    @Test
//    void findAllCourses() throws Exception {
//        Major major = createMajor("Math");
//        createCourse("C1", "Desc1", major);
//        createCourse("C2", "Desc2", major);
//
//        mockMvc.perform(get("/web/course")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", greaterThan(1)));
//    }
//
//    // ---------------- Helper Methods ----------------
//
//    private User createPerson(String firstName, String lastName, Role role) {
//        User user = User.builder()
//                .firstName(firstName)
//                .lastName(lastName)
//                .phoneNumber(randomPhone())
//                .roles(List.of(role))
//                .build();
//        return userRepository.save(user);
//    }
//
//    private Account createAccount(User user, Role role) {
//        Account account = Account.builder()
//                .username(user.getPhoneNumber())
//                .password(passwordEncoder.encode(user.getNationalCode()))
//                .state(RegisterState.ACTIVE)
//                .user(user)
//                .activeRole(role)
//                .build();
//        user.setAccount(account);
//        return accountRepository.save(account);
//    }
//
//    private String loginAndGetToken(String username, String password) throws Exception {
//        AuthenticationRequestDto loginRequest = AuthenticationRequestDto.builder()
//                .email(username)
//                .password(password)
//                .build();
//        String response = mockMvc.perform(post("/authentication/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        return objectMapper.readValue(response, AuthenticationResponse.class).getAccessToken();
//    }
//
//    private Major createMajor(String name) {
//        return majorRepository.save(Major.builder().majorName(name).majorActive(true).build());
//    }
//
//    private Course createCourse(String title, String description, Major major) {
//        return courseRepository.save(Course.builder()
//                .title(title)
//                .description(description)
//                .major(major)
//                .build());
//    }
//
//    private static String randomPhone() {
//        StringBuilder sb = new StringBuilder("09");
//        for (int i = 0; i < 9; i++) sb.append(ThreadLocalRandom.current().nextInt(0, 10));
//        return sb.toString();
//    }
//
//    private static String randomNationalCode() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 10; i++) sb.append(ThreadLocalRandom.current().nextInt(0, 10));
//        return sb.toString();
//    }
//}