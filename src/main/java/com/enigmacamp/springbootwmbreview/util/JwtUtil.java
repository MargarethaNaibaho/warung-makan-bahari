package com.enigmacamp.springbootwmbreview.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Component
public class JwtUtil {

    //private final Logger ini sama dengan pake anotasi @Slf4j
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String jwtSecret = "secret bray";
    private final String appName = "Warung Makan Bahari";

    public String generateToken(String userId){

        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes()); //getBytes ini supaya ubahnya si secret jadi ASCII byte
            String token = JWT.create()
                    .withIssuer(appName) //ssiapa yg buat kode token
                    .withSubject(userId)
                    //ini maksudnya dia akan ambil waktu sekarang, kemudian tambahkan dalam bentuk detik karna kita pake plusSeconds
                    .withExpiresAt(Instant.now().plusSeconds(60 * 60))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", "ROLE_CUSTOMER") //ini siapa yg bisa dapat akses
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception){
            log.error("Error while creating JWT token", exception.getMessage());
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
            log.error("Invalid verification JWT: ", e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getUserInfoByToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            //DecodedJWT ini interface bawaan spring security
            //ini untuk ngedecode jwt nya tadi
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject()); //getSubject untuk dapat userId
            userInfo.put("role", decodedJWT.getClaim("role")); //claim ini tadi kita set withClaim nya adalah role

            return userInfo;
        } catch (JWTVerificationException e){
            log.error("Invalid verification JWT: ", e.getMessage());
            return null;
        }
    }
}
