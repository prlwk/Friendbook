package com.friendbook.userservice.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.VerifyCode;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.VerifyCodeService;
import com.friendbook.userservice.utils.AppError;
import com.friendbook.userservice.utils.EmailText;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @RequestMapping(path = "/send-code-for-confirmation-account", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendConfirmationAccountCode(@RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId);
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
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Error sending code."), HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + userId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/send-code-for-recovery-password", method = RequestMethod.GET)
    public ResponseEntity<?> sendPasswordRecoveryCode(@RequestParam String email) {
        try {
            User user = userService.findUserByEmail(email);
            VerifyCode passwordRecoveryCode = new VerifyCode(user);
            verifyCodeService.saveCode(passwordRecoveryCode);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setText(EmailText.firstPartEmailText +
                    "Восстановление пароля" +
                    EmailText.secondPаrtEmailText +
                    "Добрый день, " + user.getName() + "! Чтобы восстановить пароль, пожалуйста, введите в приложение данный код" +
                    EmailText.thirdPаrtEmailText +
                    passwordRecoveryCode.getCode() +
                    EmailText.fourthPartEmailText, true);
            helper.setSubject("Восстановление пароля!");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Error sending code."), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
