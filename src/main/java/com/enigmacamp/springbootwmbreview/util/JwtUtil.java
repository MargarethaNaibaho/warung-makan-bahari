package com.enigmacamp.springbootwmbreview.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigmacamp.springbootwmbreview.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Logger;

@Slf4j
@Component
public class JwtUtil {

    //private final Logger ini sama dengan pake anotasi @Slf4j
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${app.spring-wmb-review.jwt-secret}") //kalo pake value ini, kita mau ambil variabel dari application.properties. ganti yg kiannya final jadi engga
    private String jwtSecret; // ini idealnya disimpan di environment juga

    //ini idealnya disimpan di environment. macam buat username dan password postgres
    //si waktu expired juga nanti dimasukkan ke dalam env variable. confignya dari application.properties
    @Value("${app.spring-wmb-review.app-name}")
    private String appName;

    @Value("${app.spring-wmb-review.jwtExpirationTimeInSecond}") //gaboleh ada operasi aritmatika dalam application.properties karna akan dianggap sebagai string
    private long jwtExpirationInSecond;

    public String generateToken(AppUser appUser){

        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes()); //getBytes ini supaya ubahnya si secret jadi ASCII byte. jadi lebih ribet secret key nya
            String token = JWT.create()
                    //ada juga .withAudience() -> artinya ini adalah untuk kasitau tentang siapa yg berhak untuk mengklaim token ini
                    .withIssuer(appName) //ssiapa yg mengeluarkan kode token ini / layanan mana yg keluarkan kode token ini
                    .withSubject(appUser.getId())
                    //ini maksudnya dia akan ambil waktu sekarang, kemudian tambahkan dalam bentuk detik karna kita pake plusSeconds
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRole().name()) //ini siapa yg bisa dapat akses
                    .sign(algorithm); //ini untuk menetapkan algoritma mana yg dipake

            return token;
        } catch (JWTCreationException exception){
            log.error("Error while creating JWT token: {}", exception.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean verifyJwtToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            //DecodedJWT ini interface bawaan spring security
            //ini untuk ngedecode jwt nya tadi
            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTVerificationException e){
            log.error("Invalid verification JWT: {}", e.getMessage());
            return false;
        }
    }

    public Map<String, String> getUserInfoByToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            //DecodedJWT ini interface bawaan spring security
            //ini untuk ngedecode jwt nya tadi
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject()); //getSubject untuk dapat userId
            userInfo.put("role", decodedJWT.getClaim("role").asString()); //claim ini tadi kita set withClaim nya adalah role

            return userInfo;
        } catch (JWTVerificationException e){
            log.error("Invalid verification JWT: {}", e.getMessage());
            return null;
        }
    }
}
