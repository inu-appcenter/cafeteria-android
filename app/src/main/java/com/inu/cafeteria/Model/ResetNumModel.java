package com.inu.cafeteria.Model;


public class ResetNumModel {

    private String fcmtoken;
    private String result;


    public ResetNumModel(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }
    public String getResult() {
        return result;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
