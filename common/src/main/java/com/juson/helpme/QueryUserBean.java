package com.juson.helpme;

import java.util.List;

/**
 * Created by cqx on 16/7/25.
 */
public class QueryUserBean {

    private String mobile;
    private String email;
    private String name;
    private String gender;

    private String role;

    private int offset = 0;
    private int limit = 10;
    private int curPage = 1;
    private int allPages;
    private String mobile_email_name;




    public String getMobile_email_name() {
        return mobile_email_name;
    }

    public void setMobile_email_name(String mobile_email_name) {
        this.mobile_email_name = mobile_email_name;
    }


    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
