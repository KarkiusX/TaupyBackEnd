package com.example.Taupyk.lt.DTO;

public class UserLoginDto {
    private String email;
    private String password;
    private boolean stayLoggedIn;

    public boolean isstayLoggedIn() {
        return stayLoggedIn;
    }

    public void setstayLoggedIn(boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
