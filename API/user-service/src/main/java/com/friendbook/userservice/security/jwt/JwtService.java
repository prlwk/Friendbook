package com.friendbook.userservice.security.jwt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
    @Autowired
    private SecretKeyProvider secretKeyProvider;

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public String accessTokenFor(String login, Integer countMinutes) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Date expiration = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(expiration);
        c.add(Calendar.MINUTE, countMinutes);
        expiration = c.getTime();
        return Jwts.builder()
                .setId(uuid)
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String refreshTokenFor(String login, Integer countMonths) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();

        String uuid = UUID.randomUUID().toString().replace("-", "");
        Date expiration = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(expiration);
        c.add(Calendar.MONTH, countMonths);
        expiration = c.getTime();
        return Jwts.builder()
                .setId(uuid)
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Jws<Claims> verify(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public Claims extractAllClaims(String token) throws URISyntaxException, IOException {
        return Jwts.parser().setSigningKey(secretKeyProvider.getKey()).parseClaimsJws(token).getBody();
    }

    public String extractUserInfo(String token) throws URISyntaxException, IOException {
        return extractAllClaims(token).getSubject();
    }
}
