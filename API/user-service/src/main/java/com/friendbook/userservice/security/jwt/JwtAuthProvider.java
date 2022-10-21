package com.friendbook.userservice.security.jwt;

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

@Component
public class JwtAuthProvider implements AuthenticationProvider {
    @Autowired
    private final JwtService jwtService;

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

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
