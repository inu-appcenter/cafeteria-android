package com.inu.cafeteria.Model;


public class RegisterResult {

    private String code;
    private String token;
    private String num;

    public RegisterResult(String code, String token, String num) {

        this.code = code;
        this.token = token;
        this.num = num;
    }

    public String getCode() {return code;}
    public String getToken() {return token;}
    public String getNum() {return num;}

    public void setCode(String code) {this.code = code;}
    public void setToken(String token) {this.token = token;}
    public void setNum(String num) {this.num = num;}
}
