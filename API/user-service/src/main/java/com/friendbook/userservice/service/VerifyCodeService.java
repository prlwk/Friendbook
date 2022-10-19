package com.friendbook.userservice.service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.VerifyCode;

public interface VerifyCodeService {
    VerifyCode findVerifyCodeByUser(User user);

    VerifyCode findVerifyCodeByUserAndCode(String email, String code);

    void deleteVerifyCodeByUser(User user);

    void saveCode(VerifyCode verifyCode);
}
