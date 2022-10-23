package com.friendbook.userservice.controller;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.userservice.service.MailService;
import com.friendbook.userservice.utils.AppError;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @RequestMapping(path = "/send-code-for-confirmation-account", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendConfirmationAccountCode(@RequestParam Long id) {
        try {
            mailService.sendConfirmationCode(id);
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error sending code."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + id + " does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/send-code-for-recovery-password", method = RequestMethod.GET)
    public ResponseEntity<?> sendPasswordRecoveryCode(@RequestParam String email) {
        try {
            mailService.sendPasswordRecoveryCode(email);
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error sending code."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
