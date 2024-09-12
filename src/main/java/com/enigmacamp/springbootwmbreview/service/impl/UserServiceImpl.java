package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.AppUser;
import com.enigmacamp.springbootwmbreview.entity.UserCredential;
import com.enigmacamp.springbootwmbreview.repository.UserCredentialRepository;
import com.enigmacamp.springbootwmbreview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;

    //verifikasi JWT
    @Override
    //ini tadi kita custom di interface UserService
    public AppUser loadUserByUserId(String id) {
        //pake usernamenotfoundexception dengan messae invalid credential untuk menutupin kalo misalnya kita buat "username not found" sbg message, maka org yg hack akan tau kalo username yg dimasukkan gadak di db
        UserCredential userCredential = userCredentialRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid credential!"));
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .build();
    }

    //verifikasi autentikasi login
    @Override
    //ini nilai kembalian yg UserDetails(dari spring)
    //kita kan implemen UserService(kita buat sendiri). si UserService ini mengextend interface UserDetailService. Di bawah inilah hasil implementasinya itu

    //knapa harus throws UsernameNotFoundException di public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    //padahal di dalam method ini, udah pake UsernameNotFoundException? Aku pun gatau :(
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid credential"));
        //kenapa bisa return AppUser padahal method ini mengharapkan UserDetails? karna AppUser ini mengimplementasi UserDetails
        //kalo return pake UserDetails.builder() dia malah error nanti
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .build();
    }
}
