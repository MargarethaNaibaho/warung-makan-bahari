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
@EnableWebSecurity
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
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("**").permitAll()
                        .anyRequest().authenticated()
                )
                //.formLogin(formLogin -> formLogin.disable()) //bisa diganti dengan .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
