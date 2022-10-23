package com.friendbook.userservice.security.jwt;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.friendbook.userservice.DTO.JwtAuthToken;
import com.friendbook.userservice.DTO.JwtAuthenticatedProfile;
import com.friendbook.userservice.exception.JwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtAuthProvider implements AuthenticationProvider {
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private SecretKeyProvider secretKeyProvider;

    public JwtAuthProvider() {
        this(null);
    }

    @Autowired
    public JwtAuthProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Jws<Claims> claim = jwtService.verify((String) authentication.getCredentials());
            String currentUser = claim.getBody().getSubject();
            return new JwtAuthenticatedProfile(currentUser);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to verify token", e);
        }
    }

    public Authentication getAuthentication(String token) throws URISyntaxException, IOException {
        Claims claims = Jwts.parser().setSigningKey(secretKeyProvider.getKey()).parseClaimsJws(token).getBody();
        String currentUser = claims.getSubject();
        return new JwtAuthenticatedProfile(currentUser);
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKeyProvider.getKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | URISyntaxException | IOException e) {
            return false;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
