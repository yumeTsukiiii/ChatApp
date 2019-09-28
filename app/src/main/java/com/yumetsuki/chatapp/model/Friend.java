package com.yumetsuki.chatapp.model;

public class Friend {

    private String name;

    private String recentMessage;

    private long recentMessageTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

    public long getRecentMessageTime() {
        return recentMessageTime;
    }

    public void setRecentMessageTime(long recentMessageTime) {
        this.recentMessageTime = recentMessageTime;
    }
}
