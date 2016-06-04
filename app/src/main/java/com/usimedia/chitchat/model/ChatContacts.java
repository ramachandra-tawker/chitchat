package com.usimedia.chitchat.model;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContacts implements Serializable {
    private String username;
    private String status;
    private Date lastseen;
    private String email;

    public ChatContacts() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "ChatContacts{" +
                "username='" + username + '\'' +
                ", status='" + status + '\'' +
                ", lastseen=" + lastseen +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatContacts that = (ChatContacts) o;
        return Objects.equal(username, that.username) &&
                Objects.equal(status, that.status) &&
                Objects.equal(lastseen, that.lastseen) &&
                Objects.equal(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, status, lastseen, email);
    }
}
