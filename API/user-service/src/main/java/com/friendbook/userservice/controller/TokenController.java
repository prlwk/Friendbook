package com.friendbook.userservice.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.userservice.DTO.EmailAndTokensBean;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.security.jwt.JwtService;
import com.friendbook.userservice.service.RefreshTokenService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.UserTokenService;
import com.friendbook.userservice.utils.AppError;

@RestController
public class TokenController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    final int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    final int REFRESH_TOKEN_EXPIRATION_TIME_MONTH = 6;

    @RequestMapping(path = "/refresh-tokens", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> refreshTokens(@RequestParam(required = false, defaultValue = "false") boolean generateRefreshToken, @RequestBody EmailAndTokensBean emailAndTokensBean) {
        User user;
        try {
            user = userService.findUserByEmail(emailAndTokensBean.getEmail());
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "The user with email " + emailAndTokensBean.getEmail() + " does not exist."), HttpStatus.NOT_FOUND);
        }

        if (!refreshTokenService.isCorrectRefreshToken(user, emailAndTokensBean.getRefreshToken())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "This refresh token does not exist."), HttpStatus.NOT_FOUND);
        }

        if (!userTokenService.isCorrectAccessToken(user, emailAndTokensBean.getAccessToken())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "This access token does not exist."), HttpStatus.NOT_FOUND);
        }


        if (!generateRefreshToken) {
            try {
                jwtService.extractAllClaims(emailAndTokensBean.getRefreshToken());
            } catch (Exception exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Refresh token expired!"), HttpStatus.NOT_FOUND);
            }
        }

        try {
            Map<String, String> map = generateMapWithInfoAboutTokens(generateRefreshToken, user,
                    emailAndTokensBean.getAccessToken(), emailAndTokensBean.getRefreshToken());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Error generating token."), HttpStatus.BAD_REQUEST);
        }
    }

    private Map<String, String> generateMapWithInfoAboutTokens(
            boolean generateRefreshToken,
            User user,
            String oldAccessToken,
            String oldRefreshToken) throws IOException, URISyntaxException {
        String newAccessToken;
        String newRefreshToken = null;
        newAccessToken = jwtService.accessTokenFor(user.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME_MINUTES);
        userTokenService.updateAccessToken(user, newAccessToken, oldAccessToken);
        if (generateRefreshToken) {
            newRefreshToken = jwtService.refreshTokenFor(user.getEmail(), REFRESH_TOKEN_EXPIRATION_TIME_MONTH);
            refreshTokenService.updateRefreshToken(user, newRefreshToken, oldRefreshToken);
        }
        Map<String, String> map = new HashMap<>();
        map.put("access-token", newAccessToken);
        map.put("expireTimeAccessToken", String.valueOf(userTokenService.getExpirationTime()));
        if (generateRefreshToken) {
            map.put("refresh-token", newRefreshToken);
            map.put("expireTimeRefreshToken", String.valueOf(refreshTokenService.getExpirationTime()));
        }
        return map;
    }
}
