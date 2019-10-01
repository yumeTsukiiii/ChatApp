package com.yumetsuki.chatapp.net.protocol.resp;

public class LoginMessage {

    private String msg;

    private String public_key;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
