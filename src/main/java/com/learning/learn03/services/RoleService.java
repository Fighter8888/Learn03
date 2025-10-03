//package com.learning.learn03.services;
//
//import com.learning.learn03.interfaces.IRoleService;
//import com.learning.learn03.models.Role;
//import com.learning.learn03.repositories.RoleRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RoleService implements IRoleService {
//    private final RoleRepository roleRepository;
//
//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    public Role findByName(String name) {
//        return roleRepository.findByRoleName(name)
//                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
//    }
//
//    public Role save(Role role) {
//        return roleRepository.save(role);
//    }
//}
