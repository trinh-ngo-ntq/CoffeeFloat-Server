package com.login.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user")
@Table(name = "user")
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String country;
    private String password;
    private String accesstoken;
    @Transient
    private String passwordConfirm;
    private String provider;
    private String avatar;
    // TrinhNX: Change gender from String to int as Dung-san requested (08/23)
    // O: other (default), 1: female, 2 : male
    private int gender;
    private String favorits;

    public int getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if("male".equalsIgnoreCase(gender)) {
            this.gender = 2;
        } else if("female".equalsIgnoreCase(gender)) {
            this.gender = 1;
        } else {
            this.gender = 0;    
        }
    }

    public String getFavorits() {
        return favorits;
    }

    public void setFavorits(String favorits) {
        this.favorits = favorits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
