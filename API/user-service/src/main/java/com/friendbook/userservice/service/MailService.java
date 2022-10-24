package com.friendbook.userservice.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendConfirmationCode(Long id) throws MessagingException;

    void sendPasswordRecoveryCode(String email) throws MessagingException;
}
