package com.juson.helpme.dao;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Range;
import org.mongodb.morphia.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by qianliang on 19/7/2016.
 */
@Entity(value = "user", noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "mobile"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field(value = "email"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field(value = "name"), options = @IndexOptions(unique = true) )
})
public class User {
    @Id
    private ObjectId id;
    @Pattern(regexp = "(^$|^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$)", message = "无效的手机号码")

    private String mobile;

    @Pattern(regexp = "(^$|^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$)", message = "无效的邮箱")
    private String email;
    private String name;
    private String gender;

    @NotSaved
    private List<String> roles = new ArrayList<>();
    @Range(min = 2 , max = 500,message = "无效的体重")
    private Double weight;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastUpdateDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date registerDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;


    private String password;
    private String salt;
    private Boolean locked;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
