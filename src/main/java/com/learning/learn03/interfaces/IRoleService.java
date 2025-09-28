package com.learning.learn03.interfaces;

import com.learning.learn03.models.Role;

public interface IRoleService {
    public Role findByName(String name);

    public Role save(Role role);
}
