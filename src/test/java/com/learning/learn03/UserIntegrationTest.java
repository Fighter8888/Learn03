package com.learning.learn03;

import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRegisterStudent() throws Exception {
        String studentRegisterJson = """
            {
              "firstName": "Ali",
              "lastName": "student",
              "email": "student@example.com",
              "password": "12345"
            }
            """;

        mockMvc.perform(post("/users/student/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentRegisterJson))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldRegisterTeacher() throws Exception {
        String teacherRegisterJson = """
            {
              "firstName": "ahamd",
              "lastName": "teacher",
              "email": "teacher@example.com",
              "password": "12345"
            }
            """;

        mockMvc.perform(post("/users/teacher/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherRegisterJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldLoginStudent() throws Exception {
        Role studentRole = roleRepository.save(Role.builder().name("student").build());

        User student = new User();
        student.setEmail("student@example.com");
        student.setPassword(passwordEncoder.encode("12345"));
        student.setRoles(List.of(studentRole));
        userRepository.save(student);

        String studentLoginJson = """
        {
          "email": "student@example.com",
          "password": "12345"
        }
    """;

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentLoginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}