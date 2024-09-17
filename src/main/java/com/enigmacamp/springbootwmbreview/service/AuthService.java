package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.AuthRequest;
import com.enigmacamp.springbootwmbreview.dto.response.LoginResponse;
import com.enigmacamp.springbootwmbreview.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(AuthRequest authRequest);
    RegisterResponse registerAdmin(AuthRequest authRequest);
    LoginResponse login(AuthRequest authRequest);
}
