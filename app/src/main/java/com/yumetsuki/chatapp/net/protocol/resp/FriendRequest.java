package com.yumetsuki.chatapp.net.protocol.resp;

public class FriendRequest {

    private int id;

    private String from;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
