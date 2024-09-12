package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

//UserDetailsService bawaan springboot
public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String id);

}
