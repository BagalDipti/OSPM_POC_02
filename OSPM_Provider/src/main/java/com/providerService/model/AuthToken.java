package com.providerService.model;

public class AuthToken {

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthToken(String token) {
        this.token = token;
    }

}