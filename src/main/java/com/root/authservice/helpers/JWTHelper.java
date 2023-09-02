package com.root.authservice.helpers;

import com.root.authservice.config.ConsulConfig;
import com.root.commondependencies.vo.UserVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTHelper {


    public static final int JWT_DEFAULT_TIMEOUT = 20;
    private final ConsulConfig config;

    @Autowired
    public JWTHelper(ConsulConfig config){
        this.config = config;
    }

    public String getJwtToken(UserVO userVO){
        String secret = config.getConfigValueByKey("JWT_SECRET");
        int timeout = config.getConfigValueByKey("JWT_TIMEOUT", JWT_DEFAULT_TIMEOUT);
        return createJWTToken(secret, timeout, userVO);
    }

    private String createJWTToken(String secret, int timeout, UserVO userVO){

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        return Jwts.builder()
                .claim("name", userVO.getName())
                .claim("email", userVO.getEmail())
                .setSubject("login")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(timeout, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }

}
