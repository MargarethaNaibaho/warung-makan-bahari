package com.enigmacamp.springbootwmbreview.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //ini yg buat method filterChain otomatis di cari konfig ini. nanti otomatis terpanggil
public class SecurityConfiguration {
    @Bean
    //goalsnya adalah link API yg dibuat akan dikasih autentikasinya, ga akan diredirect ke login terlebih dahulu yg dari spring framework
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity.httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(request ->
//                        request.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
//                                .requestMatchers("api/v1/auth/**","api/v1/image/**" , "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .build();

        //ini dah jalan
        return httpSecurity.csrf(csrf -> csrf.disable()) //arti csrf.disable ini adalah nonaktifkannya karna kita pake stateless auntentikasi(JWT)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("**").permitAll() //artinya semua request dengan URL pattern ** bisa jalan tanpa perlu autentikasi login bawaan spring templatenya
                        .anyRequest().authenticated() //selain url yg ditetapkan di atas, harus diautentikasi
                )
                //.formLogin(formLogin -> formLogin.disable()) //bisa diganti dengan .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
