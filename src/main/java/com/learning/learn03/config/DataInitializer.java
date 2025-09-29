package com.learning.learn03.config;

import com.learning.learn03.models.Role;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.services.PrincipalService;
import com.learning.learn03.services.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class DataInitializer implements CommandLineRunner {
    private final PrincipalService principalService;
    private final RoleService roleService;
    private final InitializerService initializerService;

    public DataInitializer(PrincipalService principalService, RoleService roleService, InitializerService initializerService) {
        this.principalService = principalService;
        this.roleService = roleService;
        this.initializerService = initializerService;
    }

    public void run(String... args) {
        initializerService.createRolesIfNotExist();
        initializerService.createPrincipalIfNotExists();    }
}

//@Component
//public class DataInitializer {
//    private final RoleRepository roleRepository;
//
//    public DataInitializer(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @PostConstruct
//    public void init() {
//        List<String> roles = List.of("STUDENT", "TEACHER", "ADMIN");
//
//        for (String roleName : roles) {
//            roleRepository.findByName(roleName).orElseGet(() -> {
//                Role role = new Role();
//                role.setName(roleName);
//                return roleRepository.save(role);
//            });
//        }
//    }
//}
