package com.learning.learn03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.learn03.dtos.UserDto;
import com.learning.learn03.models.Account;
import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.models.enums.UserStatus;
import com.learning.learn03.repositories.AccountRepository;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.UserRepository;
import com.learning.learn03.dtos.AuthenticationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    private UserDto newUser;



    @Test
    void studentRegister() throws Exception {
        UserDto dto = UserDto.builder()
                .firstName("ahmad")
                .lastName("ahmadi")
                .email("emial@email.com")
                .phoneNumber("123456789")
                .password("123456")
                .majorName("MADNESS")
                .build();

        mockMvc.perform(post("/authentication/student/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
        Role principalRole = roleRepository.findByRoleName("PRINCIPAL").orElseThrow();

        User admin = createAdminPerson(principalRole);

        AuthenticationRequestDto loginRequest = AuthenticationRequestDto.builder()
                .email(admin.getEmail())
                .password(admin.getPhoneNumber())
                .build();

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenType").isNotEmpty());
    }

    private User createAdminPerson(Role role) {
        User principal = User.builder()
                .firstName("principal")
                .lastName("principal")
                .phoneNumber("09300000000")
                .email("principal2@gmail.com")
                .roles(List.of(role))
                .build();

        userRepository.save(principal);

        Account account = Account.builder()
                .userName(principal.getEmail())
                .password(passwordEncoder.encode(principal.getPhoneNumber()))
                .status(UserStatus.Approved)
                .user(principal)
                .accountAuthId(UUID.randomUUID())
                .role(role)
                .build();

        principal.setAccount(account);
        accountRepository.save(account);

        return principal;
    }


}
