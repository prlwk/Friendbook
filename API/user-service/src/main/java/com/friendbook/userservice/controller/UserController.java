package com.friendbook.userservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendbook.userservice.DTO.ChangePasswordBean;
import com.friendbook.userservice.DTO.EmailAndTokensBean;
import com.friendbook.userservice.DTO.LoginBean;
import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.DTO.UserProfile;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.VerifyCode;
import com.friendbook.userservice.security.jwt.JwtService;
import com.friendbook.userservice.service.RefreshTokenService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.UserTokenService;
import com.friendbook.userservice.service.VerifyCodeService;
import com.friendbook.userservice.utils.AppError;
import com.friendbook.userservice.utils.EmailText;

@RestController
@RequestMapping("/user")
public class UserController {

    final int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    final int REFRESH_TOKEN_EXPIRATION_TIME_MONTH = 6;

    @Autowired
    private JavaMailSender javaMailSender;

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
        if (userService.isEmailExist(email)) {
            return new ResponseEntity<>("Email exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Email does not exists", HttpStatus.OK);
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> register(@RequestPart String registerBeanString, @RequestPart(value = "file", required = false) MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        RegisterBean registerBean;
        try {
            registerBean = mapper.readValue(registerBeanString, RegisterBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Failed to convert JSON object"), HttpStatus.BAD_REQUEST);
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
                File newFile = new File(path + "/user-service/src/main/resources/user-photo/" + user.getId() + ".jpg");
                file.transferTo(newFile);
                userService.setLinkPhoto(user.getId() + ".jpg", user.getId());
            } else {
                userService.setLinkPhoto("default.jpg", user.getId());
            }
        } catch (IOException e) {
            userService.deleteUser(user);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Failed to convert photo"), HttpStatus.BAD_REQUEST);
        }

        try {
            VerifyCode confirmationCode = new VerifyCode(user);
            verifyCodeService.saveCode(confirmationCode);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("plum.bestcompany@gmail.com");
            helper.setTo(user.getEmail());
            helper.setText(EmailText.firstPartEmailText +
                    "Подтверждение почты" +
                    EmailText.secondPаrtEmailText +
                    "Добрый день, " + user.getName() + "! Чтобы подтвердить свой аккаунт, нужно ввести код в приложение." +
                    EmailText.thirdPаrtEmailText +
                    confirmationCode.getCode() +
                    EmailText.fourthPartEmailText, true);
            helper.setSubject("Подтверждение аккаунта");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Error sending code."), HttpStatus.BAD_REQUEST);
        }

        try {
            Map<String, String> map =
                    generateMapWithInfoAboutTokens(user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Error generation token!"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("confirmationCode") String confirmationCode,
                                                @RequestParam("email") String email) {
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
        if (!passwordEncoder.matches(loginBean.getPassword(), user.getPassword())) {
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
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Error generation token!"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> logout(@RequestBody EmailAndTokensBean emailAndTokensBean, HttpServletRequest request) {
        if (isTokenExpired(request)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Access token expired!"), HttpStatus.NOT_FOUND);
        }
        User user;
        try {
            user = userService.findUserByEmail(emailAndTokensBean.getEmail());
            final String authorizationHeaderValue = request.getHeader("Authorization");
            if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
                String token = authorizationHeaderValue.substring(7);
                if (!userTokenService.isCorrectAccessToken(user, token)) {
                    return new ResponseEntity<>(
                            new AppError(HttpStatus.NOT_FOUND.value(),
                                    "This access token does not exist."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.BAD_REQUEST.value(),
                                "Bad authorization."), HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "The user with " + emailAndTokensBean.getEmail() + " does not exist."), HttpStatus.NOT_FOUND);
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

        userTokenService.deleteAccessToken(user, emailAndTokensBean.getAccessToken());
        refreshTokenService.deleteRefreshToken(user, emailAndTokensBean.getRefreshToken());
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
                                "Invalid code for user with email " + email), HttpStatus.BAD_REQUEST);
            }
            verifyCodeService.deleteVerifyCodeByUser(user);
            try {
                Map<String, String> map =
                        generateMapWithInfoAboutTokens(user);
                return new ResponseEntity<>(map, HttpStatus.OK);
            } catch (IOException | URISyntaxException e) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.BAD_REQUEST.value(),
                                "Error generation token!"), HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Code for user with " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordBean changePasswordBean, HttpServletRequest request) {
        if (isTokenExpired(request)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Access token expired!"), HttpStatus.NOT_FOUND);
        }
        User user;
        try {
            user = userService.findUserByEmail(changePasswordBean.getEmail());
            final String authorizationHeaderValue = request.getHeader("Authorization");
            if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
                String token = authorizationHeaderValue.substring(7);
                if (!userTokenService.isCorrectAccessToken(user, token)) {
                    return new ResponseEntity<>(
                            new AppError(HttpStatus.NOT_FOUND.value(),
                                    "This access token does not exist."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.BAD_REQUEST.value(),
                                "Bad authorization."), HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + changePasswordBean.getEmail() + " does not exist."), HttpStatus.NOT_FOUND);
        }
        userService.changePassword(changePasswordBean.getPassword(), user);
        return new ResponseEntity<>(HttpStatus.OK);
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
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("user", new UserProfile(user));
        try {
            String path = new File("").getAbsolutePath();
            File file = new File(path + "/user-service/src/main/resources/user-photo/" + user.getLinkPhoto());
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            formData.add("image", new ByteArrayResource(multipartFile.getBytes()));
        } catch (IOException e) {
            formData.add("image", null);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(formData, httpHeaders, HttpStatus.OK);
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

    private boolean isTokenExpired(HttpServletRequest request) {
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7);
            try {
                jwtService.extractAllClaims(token);
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}
