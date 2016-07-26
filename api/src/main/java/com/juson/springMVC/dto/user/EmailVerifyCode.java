package com.juson.springMVC.dto.user;

/**
 * Created by qianliang on 4/5/2016.
 */
public class EmailVerifyCode {
    private String email;
    private String verifyCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
