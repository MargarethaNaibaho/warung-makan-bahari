package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.Role;
import com.enigmacamp.springbootwmbreview.repository.RoleRepository;
import com.enigmacamp.springbootwmbreview.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(Role role) {
        //kalo dah ada role, kita akan ambil dr db
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());
        if(!optionalRole.isEmpty()){
            return optionalRole.get();
        }

        //jika role belum ada, kita create baru
        return roleRepository.save(role);
    }
}
