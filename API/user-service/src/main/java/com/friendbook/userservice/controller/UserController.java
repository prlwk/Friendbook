package com.friendbook.userservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendbook.userservice.DTO.LoginBean;
import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.security.jwt.JwtService;
import com.friendbook.userservice.service.MailService;
import com.friendbook.userservice.service.RefreshTokenService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.UserTokenService;
import com.friendbook.userservice.service.VerifyCodeService;
import com.friendbook.userservice.utils.AppError;

@RestController
@RequestMapping("/user")
public class UserController {

    private final int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    private final int REFRESH_TOKEN_EXPIRATION_TIME_MONTH = 6;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(path = "/check-email-exists", method = RequestMethod.GET)
    public ResponseEntity<?> checkEmailExists(@RequestParam("email") String email) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isEmailExist(email));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> register(@RequestPart String registerBeanString, @RequestPart(value = "file", required = false) MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        RegisterBean registerBean;
        try {
            registerBean = mapper.readValue(registerBeanString, RegisterBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert JSON object"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user;
        if (userService.isEmailExist(registerBean.getEmail())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "This email is already in use"), HttpStatus.CONFLICT);
        }
        if (userService.isLoginExist(registerBean.getLogin())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "This login is already in use"), HttpStatus.CONFLICT);
        }

        user = userService.registerUser(registerBean);
        try {
            if (file != null) {
                String path = new File("").getAbsolutePath();
                File newFile = new File(path + user.getId() + ".0.jpg");
                file.transferTo(newFile);
                userService.setLinkPhoto(user.getId() + ".0.jpg", user.getId());
            } else {
                userService.setLinkPhoto(null, user.getId());
            }
        } catch (IOException e) {
            userService.deleteUser(user);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert photo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            mailService.sendConfirmationCode(user.getId());
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error sending code."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Map<String, String> map =
                    generateMapWithInfoAboutTokens(user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error generation token!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("confirmationCode") String confirmationCode,
                                                @RequestParam("email") String email) {
        if (userService.isEmailExist(email)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "This email is already in use"), HttpStatus.CONFLICT);
        }

        try {
            User user = verifyCodeService.findVerifyCodeByUserAndCode(email, confirmationCode).getUser();
            user.setEnabled(true);
            userService.save(user);
            userService.deleteAllUsersExceptVerified(user.getEmail());
            verifyCodeService.deleteVerifyCodeByUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Code " + confirmationCode + " for user with " + email + " does not exist."), HttpStatus.NOT_FOUND);
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
        if (!userService.isCorrectPassword(user, loginBean.getPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Wrong password!"), HttpStatus.NOT_FOUND);
        }

        try {
            Map<String, String> map =
                    generateMapWithInfoAboutTokens(user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error generation token!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/password-recovery", method = RequestMethod.GET)
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
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Invalid code for user with email " + email), HttpStatus.NOT_FOUND);
            }
            verifyCodeService.deleteVerifyCodeByUser(user);
            try {
                Map<String, String> map =
                        generateMapWithInfoAboutTokens(user);
                return new ResponseEntity<>(map, HttpStatus.OK);
            } catch (IOException | URISyntaxException e) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Error generation token!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Code for user with " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUserById(id);
        } catch (EntityNotFoundException exception) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + id + " does not exist."), httpHeaders, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getInfoForUserPageWithoutEmail(user), HttpStatus.OK);
    }

    @RequestMapping(path = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getUserImage(@RequestParam Long id) {
        User user;
        try {
            user = userService.getUserById(id);
        } catch (EntityNotFoundException exception) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + id + " does not exist."), httpHeaders, HttpStatus.NOT_FOUND);
        }
        try {
            String path = new File("").getAbsolutePath();
            File file = new File(path + user.getLinkPhoto());
            InputStream input = new FileInputStream(file);
            return new ResponseEntity<>(IOUtils.toByteArray(input), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private Map<String, String> generateMapWithInfoAboutTokens(
            User user) throws IOException, URISyntaxException {
        String newAccessToken;
        String newRefreshToken;
        newAccessToken = jwtService.accessTokenFor(user.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME_MINUTES);
        userTokenService.addAccessToken(user, newAccessToken);
        newRefreshToken = jwtService.refreshTokenFor(user.getEmail(), REFRESH_TOKEN_EXPIRATION_TIME_MONTH);
        refreshTokenService.addRefreshToken(user, newRefreshToken);

        Map<String, String> map = new HashMap<>();
        map.put("access-token", newAccessToken);
        map.put("expireTimeAccessToken", String.valueOf(userTokenService.getExpirationTime()));
        map.put("refresh-token", newRefreshToken);
        map.put("expireTimeRefreshToken", String.valueOf(refreshTokenService.getExpirationTime()));
        map.put("id", String.valueOf(user.getId()));
        map.put("email", user.getEmail());
        return map;
    }
}