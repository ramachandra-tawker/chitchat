package com.usimedia.chitchat.model;

/**
 * Created by USI IT on 6/4/2016.
 */
public class Column {
    private String name;
    private  String type;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setNsme(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
