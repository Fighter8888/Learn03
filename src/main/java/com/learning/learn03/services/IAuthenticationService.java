package com.learning.learn03.services;

import com.learning.learn03.base.IBaseService;
import com.learning.learn03.dtos.AuthenticationRequestDto;
import com.learning.learn03.dtos.AuthenticationResponseDto;
import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import java.security.Principal;
import java.util.List;

public interface IAuthenticationService extends IBaseService<User, Integer> {
    AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto);

    void changeRole(String username, String roleName);

    void addRoleToPerson(String role, Integer userId);

    void activeAccount(Integer id);

    void inactiveAccount(Integer id);

    List<Role> getUserRoles(Principal principal);
}