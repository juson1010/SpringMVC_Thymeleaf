package com.juson.springMVC.dto.user;

/**
 * Created by qianliang on 2/3/2016.
 */
public class MobileVerifyCode {
    private String mobile;
    private String verifyCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
