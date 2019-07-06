package com.inu.cafeteria.Model;

public class LoginData {


    private String sno;
    private String pw;
    private String auto;
    private String token;
    private String device;

    public LoginData(String sno,
                     String pw,
                     boolean auto,
                     String token,
                     String device) {
        this.sno = sno;
        this.pw = pw;
        if(auto) {
            this.auto = "1";
        } else {
            this.auto = "0";
        }
        this.token = token;
        this.device = device;
    }


    public String getSno() {
        return sno;
    }
    public String getPw() {
        return pw;
    }
    public String getAuto() {
        return auto;
    }
    public String getToken() {
        return token;
    }
    public String getDevice() {
        return device;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setAuto(String auto) {
        this.auto = auto;
    }
    public void setToken(String token) {
        this.token= token;
    }
    public void setDevice(String device) {
        this.device = device;
    }
}
