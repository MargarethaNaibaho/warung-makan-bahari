package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.constant.ERole;
import com.enigmacamp.springbootwmbreview.dto.request.AuthRequest;
import com.enigmacamp.springbootwmbreview.dto.response.LoginResponse;
import com.enigmacamp.springbootwmbreview.dto.response.RegisterResponse;
import com.enigmacamp.springbootwmbreview.entity.AppUser;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager; //ini diambil dari bean yg kita buat di SecurityConfiguration.java

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
                    .username(authRequest.getUsername().toLowerCase()) //ini supaya semua username baru yg didaftarkan dijadikan lowerCase
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
        //tempat untuk logic login
        validationUtil.validate(authRequest);

        //Authentication merupakan interface dari spring framework
        //AuthenticationManager juga adalah interface
        //UsernamePasswordAuthenticationToken diake untuk tampung kredensialnya yaitu (username dan password)
        //parameter pertama akan masuk sebagai variable principal. ini akan berisi username (untuk kasus ini)
        //parameter kedua akan masuk sebagai variabel credential. ini akan berisi password (untuk kasus ini)
        //authenticate method ini akan ambil AuthenticationPrivoder. Spring biasanya pake DaoAuthenticationProvider untuk liat db kita apakah dah pas atau engga
        //dia akan ambil principal (si username) dan credential (si password)
        //si AuthenticationProvider ini akan ambil data dari UserDetails (ini isinya dari yg ditarik spring dari db) kemudian dia coocknkan ke data credential dan principal
        //kalo misalnya berhasil, maka dia akan kembalikan Authentication objek. kalo ga, dia lempar BadCredentialsException
        //isi dari Authentication nanti adalah principal, credential, dan authorities
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));

        //ini untuk masukkan hasil autentikasi ke spring security context
        //hasil autentikasi di atas akan disimpan secara global dari Spring Security yg nyimpan informasi autentikasi supaya dapat diakses di mana pun di dalam aplikasi
        //setiiap user punya thread tersendiri (dikasih springboot) sehingga kalo ada beberapa useryg hit api yg sama, ga bakal ada hubungannya dengan db langsung. itu akan dibuat masing" thread untuk masing-masing user
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        //tempat logic untuk login
        //dicasting dulu supaya bisa dapat principal (ambil objek dari si appUser)
        AppUser appUser = (AppUser) authenticate.getPrincipal();
        String token = jwtUtil.generateToken(appUser); //nnti kalo dah ada transaksi login, param ini kita isi dengan userId yg beneran
        return LoginResponse.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}
