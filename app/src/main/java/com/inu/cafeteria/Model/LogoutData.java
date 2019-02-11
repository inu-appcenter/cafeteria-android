package com.inu.cafeteria.Model;

public class LogoutData {

    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }


    public LogoutData(String token) {
        this.token = token;
    }

    public LogoutData() {
    }
}
