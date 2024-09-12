package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.Role;

public interface RoleService {
    Role getOrSave(Role role);
}
