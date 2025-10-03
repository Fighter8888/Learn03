package com.learning.learn03.config;

import com.learning.learn03.models.Principal;
import com.learning.learn03.models.Role;
import com.learning.learn03.repositories.PrincipalRepository;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InitializerService {

    private final static String NOT_FOUND = "%s not found!";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrincipalRepository principalRepository;
    private final PasswordEncoder passwordEncoder;

    public InitializerService(UserRepository userRepository, RoleRepository roleRepository, PrincipalRepository principalRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.principalRepository = principalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPrincipalIfNotExists() {
        Role principalRole = roleRepository.findByRoleName("PRINCIPAL")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        if (userRepository.findByRoles(principalRole).isEmpty()) {
            Principal principal = new Principal();
            principal.setEmail("principal@gmail.com");
            principal.setPassword(passwordEncoder.encode("password"));
            principal.setRoles(List.of(principalRole));
            principalRepository.save(principal);
        }
    }

    public void createRolesIfNotExist() {
        Optional<Role> principal = roleRepository.findByRoleName("PRINCIPAL");
        Optional<Role> teacher = roleRepository.findByRoleName("TEACHER");
        Optional<Role> student = roleRepository.findByRoleName("STUDENT");
        if (student.isEmpty() && teacher.isEmpty() && principal.isEmpty()) {
            roleRepository.save(Role.builder().name("PRINCIPAL").build());
            roleRepository.save(Role.builder().name("TEACHER").build());
            roleRepository.save(Role.builder().name("STUDENT").build());
        }
    }


}