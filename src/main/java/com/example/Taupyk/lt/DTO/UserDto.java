package com.example.Taupyk.lt.DTO;

public class UserDto {

    private long uid;

    private String name;

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public UserDto(long uid, String name, boolean admin) {
        this.uid = uid;
        this.name = name;
        this.admin = admin;
    }
    public UserDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
