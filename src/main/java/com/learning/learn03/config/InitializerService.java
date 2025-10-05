package com.learning.learn03.config;

import com.learning.learn03.models.*;
import com.learning.learn03.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InitializerService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final MajorRepository majorRepository;

    public InitializerService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, MajorRepository majorRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.majorRepository = majorRepository;
    }


    public void createPrincipalIfNotExists() {
        Role principalRole = roleRepository.findByRoleName("PRINCIPAL")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        if (userRepository.findByRoles(principalRole).isEmpty()) {
            User principal = User.builder()
                    .email("principal@gmail.com")
                    .firstName("Principal")
                    .lastName("Principal")
                    .phoneNumber("09301234567")
                    .roles(List.of(principalRole, userRole))
                    .build();
            userRepository.save(principal);


            Account principalAccount = Account.builder()
                    .userName("principal@gmail.com")
                    .password(passwordEncoder.encode("principal"))
                    .status(UserStatus.Approved)
                    .user(principal)
                    .role(principalRole)
                    .build();

            principal.setAccount(principalAccount);
            accountRepository.save(principalAccount);
        }
    }

    public void createRolesIfNotExist() {

        Optional<Role> principal = roleRepository.findByRoleName("PRINCIPAL");
        Optional<Role> admin = roleRepository.findByRoleName("ADMIN");
        Optional<Role> teacher = roleRepository.findByRoleName("TEACHER");
        Optional<Role> student = roleRepository.findByRoleName("STUDENT");
        Optional<Role> manager = roleRepository.findByRoleName("MANAGER");
        Optional<Role> user = roleRepository.findByRoleName("USER");


        if (student.isEmpty() && teacher.isEmpty() && principal.isEmpty()) {
            roleRepository.save(Role.builder().roleName("PRINCIPAL").build());
            roleRepository.save(Role.builder().roleName("TEACHER").build());
            roleRepository.save(Role.builder().roleName("STUDENT").build());
            roleRepository.save(Role.builder().roleName("MANAGER").build());
            roleRepository.save(Role.builder().roleName("USER").build());
        }
    }

    public void createMajorIfNotExists() {
        Major computer = Major.builder().majorName("MADNESS")
                .majorActive(true).majorCode(UUID.randomUUID()).build();
        if (majorRepository.findByMajorName(computer.getMajorName()).isEmpty()) {
            majorRepository.save(computer);
        }
    }


}