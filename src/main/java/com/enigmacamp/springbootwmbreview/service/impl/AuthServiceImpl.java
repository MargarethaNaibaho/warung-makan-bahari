package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.constant.ERole;
import com.enigmacamp.springbootwmbreview.dto.request.AuthRequest;
import com.enigmacamp.springbootwmbreview.dto.response.LoginResponse;
import com.enigmacamp.springbootwmbreview.dto.response.RegisterResponse;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.entity.Role;
import com.enigmacamp.springbootwmbreview.entity.UserCredential;
import com.enigmacamp.springbootwmbreview.repository.UserCredentialRepository;
import com.enigmacamp.springbootwmbreview.service.AuthService;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import com.enigmacamp.springbootwmbreview.service.RoleService;
import com.enigmacamp.springbootwmbreview.util.JwtUtil;
import com.enigmacamp.springbootwmbreview.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil; //ini tadi dah kita buat file nya di package util
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest authRequest) {
        try{
            validationUtil.validate(authRequest);
            //role
            Role role = roleService.getOrSave(Role.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build());

            //user credential
            UserCredential userCredential = UserCredential.builder()
                    .username(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //customer
            Customer customer = Customer.builder()
                    .userCredential(userCredential)
                    .build();

            customerService.createNewCustomer(customer);
            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        } catch(DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists!");
        }

    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        validationUtil.validate(authRequest);
        //tempat logic untuk login
        String token = jwtUtil.generateToken("id"); //nnti kalo dah ada transaksi login, param ini kita isi dengan userId yg beneran
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
