package com.learning.learn03;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrincipalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User pendingUser;
    private String jwtToken;
    @Autowired
    private PasswordEncoder passwordEncoder ;

    @BeforeEach
    void setUp() throws Exception {
        // ساخت Role
        Role principalRole = roleRepository.save(Role.builder().name("PRINCIPAL").build());

        // ساخت کاربر principal
        User principal = User.builder()
                .firstName("Admin")
                .lastName("Principal")
                .email("principal@example.com")
                .password(passwordEncoder.encode("12345"))
                .status(UserStatus.Approved)
                .roles(List.of(principalRole))
                .build();
        userRepository.save(principal);

        // لاگین و گرفتن توکن
        jwtToken = loginAndGetJwtToken("principal@example.com", "12345");

        // ساخت یوزر Pending
        pendingUser = userRepository.save(User.builder()
                .firstName("Pending")
                .lastName("User")
                .email("pending@example.com")
                .password(passwordEncoder.encode("12345"))
                .status(UserStatus.Pending)
                .build());
    }

    private String loginAndGetJwtToken(String email, String password) throws Exception {
        String loginJson = """
            {
              "email": "%s",
              "password": "%s"
            }
            """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("accessToken").asText();
    }

    @Test
    void shouldReturnPendingUsers() throws Exception {
        mockMvc.perform(get("/principal/pending")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Pending"));
    }

    @Test
    void shouldUpdateUserStatus() throws Exception {
        mockMvc.perform(put("/principal/{id}/status", pendingUser.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("status", "Approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Approved"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        pendingUser.setFirstName("UpdatedName");

        mockMvc.perform(put("/principal/{id}", pendingUser.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pendingUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedName"));
    }
}

//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class PrincipalControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    private User pendingUser;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    void setUp() {
//
//        pendingUser = new User();
//        pendingUser.setFirstName("TestUser");
//        pendingUser.setStatus(UserStatus.Pending);
//        pendingUser = userRepository.save(pendingUser);
//        Role principalRole = roleRepository.save(Role.builder().name("principal").build());
//
//        User principal = User.builder()
//                .email("principal@example.com")
//                .password(passwordEncoder.encode("12345"))
//                .roles(List.of(principalRole))
//                .status(UserStatus.Approved)
//                .build();
//
//        userRepository.save(principal);
//    }
//
//    @Test
//    void shouldReturnPendingUsersWithValidJwt() throws Exception {
//        // 1. login
//        String loginJson = """
//            {
//              "email": "principal@example.com",
//              "password": "12345"
//            }
//            """;
//
//        MvcResult loginResult = mockMvc.perform(post("/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").exists())
//                .andReturn();
//
//        String responseBody = loginResult.getResponse().getContentAsString();
//        String token = new ObjectMapper().readTree(responseBody).get("accessToken").asText();
//
//        // 2. call protected endpoint with JWT
//        mockMvc.perform(get("/principal/pending")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    void shouldUpdateUserStatus() throws Exception {
//        mockMvc.perform(put("/principal/{id}/status", pendingUser.getId())
//                        .param("status", "APPROVED"))  // دقت کن به حروف بزرگ
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("APPROVED"));
//    }
//
//    @Test
//    void shouldUpdateUser() throws Exception {
//        pendingUser.setFirstName("UpdatedName");
//
//        mockMvc.perform(put("/principal/{id}", pendingUser.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(pendingUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("UpdatedName"));
//    }
//}