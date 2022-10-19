package com.friendbook.userservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendbook.userservice.DTO.ChangePasswordBean;
import com.friendbook.userservice.DTO.EmailAndTokensBean;
import com.friendbook.userservice.DTO.LoginBean;
import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.RefreshToken;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserToken;
import com.friendbook.userservice.model.VerifyCode;
import com.friendbook.userservice.security.JwtService;
import com.friendbook.userservice.service.EmailSenderService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.VerifyCodeService;
import com.friendbook.userservice.utils.AppError;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(path = "/registration", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> register(@RequestPart String registerBeanString, @RequestPart(value = "file", required = false) MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        RegisterBean registerBean;
        try {
            registerBean = mapper.readValue(registerBeanString, RegisterBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Failed to convert JSON object"), HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        try {
            if (userService.isEmailExist(registerBean.getEmail())) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This email is already in use"), HttpStatus.CONFLICT);
            }
            if (userService.isLoginExist(registerBean.getLogin())) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This login is already in use"), HttpStatus.CONFLICT);
            }

            user = userService.registerUser(registerBean);

            if (file != null) {
                String path = new File("").getAbsolutePath();
                File newFile = new File(path + "/user-service/src/main/resources/user-photo" + user.getId() + ".jpg");
                file.transferTo(newFile);
                userService.setLinkPhoto(user.getId() + ".jpg", user.getId());
            } else {
                userService.setLinkPhoto("default.png", user.getId());
            }
            VerifyCode confirmationCode = new VerifyCode(user);

            verifyCodeService.saveCode(confirmationCode);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("plum.bestcompany@gmail.com");
            mailMessage.setText("Чтобы подтвердить регистрацию, пожалуйста, введите код в приложение: "
                    + confirmationCode.getCode());
            emailSenderService.sendEmail(mailMessage);
            String token;
            String refreshToken;
            try {
                token = jwtService.tokenFor(registerBean.getLogin());
                refreshToken = jwtService.refreshTokenFor(user.getLogin());
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Authorization", "Bearer " + token);
                userService.addToken(user, token);
                userService.addRefreshToken(user, refreshToken);
                Map<String, String> map = new HashMap<>();
                map.put("JWT", token);
                map.put("Refresh token", refreshToken);
                return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
            } catch (IOException | URISyntaxException e) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Error generation token!"), HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            userService.deleteUser(user);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Failed to convert photo"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("confirmationCode") String confirmationCode, @RequestParam("email") String email) {
        User user;
        try {
            user = userService.findUserByEmail(email);
        } catch (EntityNotFoundException exception) {
            user = null;
        }

        if (user != null) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + email + " exists."), HttpStatus.NOT_FOUND);
        }

        try {
            User u = verifyCodeService.findVerifyCodeByUserAndCode(email, confirmationCode).getUser();
            u.setEnabled(true);
            userService.save(u);
            verifyCodeService.deleteVerifyCodeByUser(u);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Code " + confirmationCode + " for user with" + email + "does not exist."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginBean loginBean) {
        User user;
        if (loginBean.getIsEntryByEmail()) {
            try {
                user = userService.findUserByEmail(loginBean.getLoginOrEmail());
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "The user with such email does not exist."), HttpStatus.NOT_FOUND);
            }
        } else {
            try {
                user = userService.findUserByLogin(loginBean.getLoginOrEmail());
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "The user with such nickname does not exist."), HttpStatus.NOT_FOUND);
            }
        }
        if (!passwordEncoder.matches(loginBean.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Wrong password!"), HttpStatus.NOT_FOUND);
        }

        String token;
        String refreshToken;
        try {
            token = jwtService.tokenFor(user.getLogin());
            refreshToken = jwtService.refreshTokenFor(user.getLogin());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + token);
            userService.addToken(user, token);
            userService.addRefreshToken(user, refreshToken);
            Map<String, String> map = new HashMap<>();
            map.put("access-token", token);
            map.put("refresh-token", refreshToken);
            return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Error generation token!"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/refresh-access-token", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> refreshAccessToken(@RequestBody EmailAndTokensBean emailAndTokensBean) {
        String newToken;
        if (StringUtils.isNotBlank(emailAndTokensBean.getRefreshToken()) && StringUtils.isNotBlank(emailAndTokensBean.getLogin())) {
            User user;
            try {
                user = userService.findUserByLogin(emailAndTokensBean.getLogin());
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "The user with such nickname does not exist."), HttpStatus.NOT_FOUND);
            }
            Set<RefreshToken> refreshTokenSet = user.getRefreshTokens();
            Iterator<RefreshToken> iter = refreshTokenSet.iterator();
            boolean isValidRefreshToken = false;
            while (iter.hasNext()) {
                RefreshToken refreshToken = iter.next();
                if (refreshToken.getToken().equals(emailAndTokensBean.getRefreshToken())) {
                    isValidRefreshToken = true;
                }
            }

            if (!isValidRefreshToken) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This refresh token does not exist."), HttpStatus.NOT_FOUND);
            }

            Set<UserToken> tokenSet = user.getUserTokens();
            Iterator<UserToken> it = tokenSet.iterator();
            boolean isValidToken = false;
            while (it.hasNext()) {
                UserToken userToken = it.next();
                if (userToken.getToken().equals(emailAndTokensBean.getAccessToken())) {
                    isValidToken = true;
                }
            }

            if (!isValidToken) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This token does not exist."), HttpStatus.NOT_FOUND);
            }

            try {
                Jws<Claims> claim = jwtService.verify(emailAndTokensBean.getRefreshToken());
                String userName = claim.getBody().getSubject();
                if (null == userName || !userName.equals(emailAndTokensBean.getLogin())) {
                    return new ResponseEntity<>(
                            new AppError(HttpStatus.NOT_FOUND.value(),
                                    "User info does not correct."), HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Refresh token expired!"), HttpStatus.NOT_FOUND);
            }

            try {
                newToken = jwtService.tokenFor(user.getLogin());
            } catch (IOException | URISyntaxException e) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Error generating token."), HttpStatus.NOT_FOUND);
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + newToken);
            userService.updateAccessToken(user, newToken, emailAndTokensBean.getAccessToken());
            Map<String, String> map = new HashMap<>();
            map.put("access-token", newToken);
            return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Empty refresh token."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/refresh-tokens", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> refreshToken(@RequestBody EmailAndTokensBean emailAndTokensBean) {
        String newToken;
        String newRefreshToken;
        if (StringUtils.isNotBlank(emailAndTokensBean.getRefreshToken()) && StringUtils.isNotBlank(emailAndTokensBean.getLogin())) {
            User user;
            try {
                user = userService.findUserByLogin(emailAndTokensBean.getLogin());
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "The user with such nickname does not exist."), HttpStatus.NOT_FOUND);
            }
            Set<RefreshToken> refreshTokenSet = user.getRefreshTokens();
            Iterator<RefreshToken> iter = refreshTokenSet.iterator();
            boolean isValidRefreshToken = false;
            while (iter.hasNext()) {
                RefreshToken refreshToken = iter.next();
                if (refreshToken.getToken().equals(emailAndTokensBean.getRefreshToken())) {
                    isValidRefreshToken = true;
                }
            }

            if (!isValidRefreshToken) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This refresh token does not exist."), HttpStatus.NOT_FOUND);
            }

            Set<UserToken> tokenSet = user.getUserTokens();
            Iterator<UserToken> it = tokenSet.iterator();
            boolean isValidToken = false;
            while (it.hasNext()) {
                UserToken userToken = it.next();
                if (userToken.getToken().equals(emailAndTokensBean.getAccessToken())) {
                    isValidToken = true;
                }
            }

            if (!isValidToken) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "This token does not exist."), HttpStatus.NOT_FOUND);
            }

            try {
                Jws<Claims> claim = jwtService.verify(emailAndTokensBean.getRefreshToken());
                String userName = claim.getBody().getSubject();
                if (null == userName || !userName.equals(emailAndTokensBean.getLogin())) {
                    return new ResponseEntity<>(
                            new AppError(HttpStatus.NOT_FOUND.value(),
                                    "User info does not correct."), HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Refresh token expired!"), HttpStatus.NOT_FOUND);
            }

            try {
                newToken = jwtService.tokenFor(user.getLogin());
                newRefreshToken = jwtService.refreshTokenFor(user.getLogin());
            } catch (IOException | URISyntaxException e) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Error generating token."), HttpStatus.NOT_FOUND);
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + newToken);
            userService.updateAccessToken(user, newToken, emailAndTokensBean.getAccessToken());
            userService.updateRefreshToken(user, newRefreshToken, emailAndTokensBean.getRefreshToken());
            Map<String, String> map = new HashMap<>();
            map.put("access-token", newToken);
            map.put("refresh-token", emailAndTokensBean.getRefreshToken());
            return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Empty refresh token."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> logout(@RequestBody EmailAndTokensBean emailAndTokensBean) {
        User user;
        try {
            user = userService.findUserByLogin(emailAndTokensBean.getLogin());
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "The user with such nickname does not exist."), HttpStatus.NOT_FOUND);
        }
        Set<RefreshToken> refreshTokenSet = user.getRefreshTokens();
        Iterator<RefreshToken> iter = refreshTokenSet.iterator();
        boolean isValidRefreshToken = false;
        while (iter.hasNext()) {
            RefreshToken refreshToken = iter.next();
            if (refreshToken.getToken().equals(emailAndTokensBean.getRefreshToken())) {
                isValidRefreshToken = true;
            }
        }

        if (!isValidRefreshToken) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "This refresh token does not exist."), HttpStatus.NOT_FOUND);
        }

        Set<UserToken> tokenSet = user.getUserTokens();
        Iterator<UserToken> it = tokenSet.iterator();
        boolean isValidToken = false;
        while (it.hasNext()) {
            UserToken userToken = it.next();
            if (userToken.getToken().equals(emailAndTokensBean.getAccessToken())) {
                isValidToken = true;
            }
        }

        if (!isValidToken) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "This token does not exist."), HttpStatus.NOT_FOUND);
        }

        userService.deleteToken(user, emailAndTokensBean.getAccessToken());
        userService.deleteRefreshToken(user, emailAndTokensBean.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/send-code-for-recovery-password", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendPasswordRecoveryCode(@RequestParam String email) {
        try {
            VerifyCode passwordRecoveryCode = new VerifyCode(userService.findUserByEmail(email));
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Восстановление пароля!");
            mailMessage.setFrom("plum.bestcompany@gmail.com");
            mailMessage.setText("Чтобы восстановить пароль, пожалуйста, введите в приложение данный код: " +
                    passwordRecoveryCode.getCode());
            emailSenderService.sendEmail(mailMessage);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email" + email + "does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/password-recovery", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> recoverPassword(@RequestParam String email, @RequestParam String code) {
        User user;
        try {
            user = userService.findUserByEmail(email);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email" + email + "does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            if (!verifyCodeService.findVerifyCodeByUser(user).getCode().equals(code)) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.BAD_REQUEST.value(),
                                "Invalid code for user with" + email), HttpStatus.BAD_REQUEST);
            }
            verifyCodeService.deleteVerifyCodeByUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Code for user with" + email + "does not exist."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordBean changePasswordBean) {
        User user;
        try {
            user = userService.findUserByEmail(changePasswordBean.getEmail());
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email" + changePasswordBean.getEmail() + "does not exist."), HttpStatus.NOT_FOUND);
        }
        userService.changePassword(changePasswordBean.getPassword(), user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
