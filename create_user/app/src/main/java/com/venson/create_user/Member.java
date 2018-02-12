package com.venson.create_user;

import java.io.Serializable;

public class Member implements Serializable{
    private String userId;
    private String password;
    private String email;

    public Member(String userId, String password, String email) {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
