package com.enigmacamp.springbootwmbreview.security;

import com.enigmacamp.springbootwmbreview.service.UserService;
import com.enigmacamp.springbootwmbreview.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
//class ini dipanggil di SecurityConfiguration nantinya
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    //ini dioverride dari class OncePerRequestFilter (ini abstract class makanya Si Once.... hanya bisa diwariskan tanpa bisa diobjekkan)
    @Override
    //ini cara lama dapat @Request gitu gitu
    //tujjuannya
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            //validasi token jwt
            String headerAuth = request.getHeader("Authorization"); //untuk ambil header yg ada tulisan Authorization (iini valuenya adalah token jwt. ada keywordnya nanti pas buka header di postman)
            String token = null;

            if(headerAuth != null && headerAuth.startsWith("Bearer ")){
                token = headerAuth.substring(7); //ini kenapa 7, nanti tokennya diawali dengan tulisan "Bearer " ini ada 7 karakter. setelahnya ada tokennya. kita cuma ambil token setelah tulisan Bearer itu
            }

            if(token != null && jwtUtil.verifyJwtToken(token)){
                //set authentication ke Spring Security
                Map<String, String> userInfo = jwtUtil.getUserInfoByToken(token);

                UserDetails userDetails = userService.loadUserByUserId(userInfo.get("userId"));
                //validasi / authentication by token (untuk cek username dan password)
                //usernamepassword.... itu bawaan dr spring
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //karna gadak password, kita kasih null aja
                        userDetails.getAuthorities() //user yg dah kita buat tadi dikasih otoritas
                );

                //menambahkan informasi tambahan berupa alamat IP address ataupun host host ke bentuk spring security
                authenticationToken.setDetails(new WebAuthenticationDetails(request));

                //menyimpan authentication ke spring seecurity contect
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        } catch (Exception e){
            log.error("Cannot set user authentication: {}",e.getMessage());
        }

        //ini gunanya untuk melanjutkan filter ke controller / filter lain
        filterChain.doFilter(request, response);
    }
}
