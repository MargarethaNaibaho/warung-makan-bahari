package com.enigmacamp.springbootwmbreview.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity //ini yg buat method filterChain otomatis di cari konfig ini. nanti otomatis terpanggil
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    //ini diambil dari class AuthTokenFilter yg kita buat sendiri
    private final AuthTokenFilter authTokenFilter;

    @Bean
    //ini bawaan dari spring security
    //si AunthenticationaManager ini masih dalam bentuk interface, makanya kita buat jadi bean. ini nanti yg dipanggil di AuthServiceImpl
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    //goalsnya adalah link API yg dibuat akan dikasih autentikasinya, ga akan diredirect ke login terlebih dahulu yg dari spring framework
    //tengok penjelasan yg di bawahnya lagi
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
        return httpSecurity
//                .httpBasic(withDefaults())
                .httpBasic(base -> base.disable())
                .csrf(csrf -> csrf.disable()) //arti csrf.disable ini adalah nonaktifkannya karna kita pake stateless auntentikasi(JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //ini artinya kita gabakal buat suatu session
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll() //artinya semua request dengan URL pattern ** bisa jalan tanpa perlu autentikasi login bawaan spring templatenya
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/martha/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated() //selain url yg ditetapkan di atas, harus diautentikasi
                )
                //.formLogin(formLogin -> formLogin.disable()) //bisa diganti dengan .formLogin(AbstractHttpConfigurer::disable)

                //guna filter ini adalah sebagai middleware. jadi, sebelum hit controller untuk request sesuatu, filter ini dipanggil dulu.
                //dia bisa nyaring apapun nantinya
                //di sini ada dua filter
                //authTokenFilter ini untuk validasi token jwt kita ke spring
                //usernamepasswordauthentication... ini untuk validasi decode payload dari jwt token
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
