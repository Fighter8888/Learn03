package com.learning.learn03.interfaces;

import com.learning.learn03.base.IBaseService;
import com.learning.learn03.dtos.RequestDTO;
import com.learning.learn03.dtos.ResponseDTO;
import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;

import java.security.Principal;
import java.util.List;

public interface IAuthenticationService extends IBaseService<User, Integer> {
    ResponseDTO login(RequestDTO authRequestDTO);
    void changeRole(String username , String roleName);
    void addRoleToPerson(String role , int userId);
    void activeAccount(int id);
    void inactiveAccount(int id);
    List<Role> getPersonRoles(Principal principal);
}
