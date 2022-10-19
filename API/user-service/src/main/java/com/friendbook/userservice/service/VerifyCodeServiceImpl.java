package com.friendbook.userservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.VerifyCode;
import com.friendbook.userservice.repository.VerifyCodeRepository;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    VerifyCodeRepository verifyCodeRepository;

    @Override
    public VerifyCode findVerifyCodeByUser(User user) {
        Optional<VerifyCode> passwordRecoveryCodeOptional =
                verifyCodeRepository.findVerifyCodeByUser(user);
        if (passwordRecoveryCodeOptional.isPresent()) {
            return passwordRecoveryCodeOptional.get();
        }
        throw new EntityNotFoundException("Code for this user does not exist.");
    }

    @Override
    public VerifyCode findVerifyCodeByUserAndCode(String email, String code) {
        List<VerifyCode> list = verifyCodeRepository.findVerifyCodesByCode(code);
        for (VerifyCode verifyCode : list) {
            if (verifyCode.getUser().getEmail().equals(email)) {
                return verifyCode;
            }
        }
        throw new EntityNotFoundException("Such code does not exist.");
    }

    @Override
    public void deleteVerifyCodeByUser(User user) {
        verifyCodeRepository.deleteVerifyCodeByUser(user.getId());
    }

    @Override
    public void saveCode(VerifyCode verifyCode) {
        verifyCodeRepository.save(verifyCode);
    }
}
