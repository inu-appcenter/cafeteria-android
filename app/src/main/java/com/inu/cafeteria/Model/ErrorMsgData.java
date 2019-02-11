package com.inu.cafeteria.Model;

public class ErrorMsgData {

    private String sno;
    private String msg;
    private String device;
    private String service;

    public ErrorMsgData(String sno, String msg, String device, String service) {
        this.sno = sno;
        this.msg = msg;
        this.device = device;
        this.service = service;
    }

    public String getSno() {
        return sno;
    }
    public String getMsg() {
        return msg;
    }
    public String getDevice() {
        return device;
    }
    public String getService() {
        return service;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public void setService(String service) {
        this.service = service;
    }
}
