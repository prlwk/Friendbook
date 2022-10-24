package com.friendbook.userservice.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.VerifyCode;
import com.friendbook.userservice.utils.EmailText;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    public void sendConfirmationCode(Long id) throws MessagingException {
        User user = userService.getUserById(id);
        verifyCodeService.deleteVerifyCodeByUser(user);
        VerifyCode confirmationCode = new VerifyCode(user);
        verifyCodeService.saveCode(confirmationCode);
        String text = EmailText.firstPartEmailText +
                "Подтверждение почты" +
                EmailText.secondPаrtEmailText +
                "Добрый день, " + user.getName() + "! Чтобы подтвердить свой аккаунт, нужно ввести код в приложение." +
                EmailText.thirdPаrtEmailText +
                confirmationCode.getCode() +
                EmailText.fourthPartEmailText;
        sendCode(user.getEmail(), text, "Подтверждение аккаунта!");
    }

    @Override
    public void sendPasswordRecoveryCode(String email) throws MessagingException {
        User user = userService.findUserByEmail(email);
        verifyCodeService.deleteVerifyCodeByUser(user);
        VerifyCode passwordRecoveryCode = new VerifyCode(user);
        verifyCodeService.saveCode(passwordRecoveryCode);
        String text = EmailText.firstPartEmailText +
                "Восстановление пароля" +
                EmailText.secondPаrtEmailText +
                "Добрый день, " + user.getName() + "! Чтобы восстановить пароль, пожалуйста, введите в приложение данный код" +
                EmailText.thirdPаrtEmailText +
                passwordRecoveryCode.getCode() +
                EmailText.fourthPartEmailText;
        sendCode(email, text, "Восстановление пароля!");
    }

    private void sendCode(String email, String text, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setText(text, true);
        helper.setSubject(subject);
        javaMailSender.send(message);
    }
}
