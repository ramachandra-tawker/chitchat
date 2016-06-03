package com.usimedia.chitchat.model;

import java.util.Date;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContacts {
    private String username;
    private String status;
    private Date lastseen;

    public ChatContacts() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastseen() {
        return lastseen;
    }

    public void setLastseen(Date lastseen) {
        this.lastseen = lastseen;
    }
}
