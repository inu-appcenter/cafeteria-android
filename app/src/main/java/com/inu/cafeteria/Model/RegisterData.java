package com.inu.cafeteria.Model;

import java.io.Serializable;

public class RegisterData implements Serializable{

    private String code;
    private String num1;
    private String num2;
    private String num3;
    private String token;
    private String device;

    public RegisterData(String code, String num1, String num2, String num3, String token, String device) {
        this.code = code;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.token = token;
        this.device = device;
    }


    public String getCode() {
        return code;
    }
    public String getNum1() {
        return num1;
    }
    public String getNum2() {
        return num2;
    }
    public String getNum3() {
        return num3;
    }
    public String getToken() {
        return token;
    }
    public String getDevice() {return device;}

    public void setCode(String code) {
        this.code = code;
    }
    public void setNum1(String num1) {
        this.num1 = num1;
    }
    public void setNum2(String num2) {
        this.num2 = num2;
    }
    public void setNum3(String num3) {
        this.num3 = num3;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setDevice(String device) {this.device = device;}

}
