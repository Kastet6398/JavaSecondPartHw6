package com.example.demo.lib.models;

public class User extends BaseModel{
    private final String login;
    private final String email;
    private final String password;

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.email = email;
        this.password = password;
    }


    public User(String login, String password, String email, int id) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id;
    }
}
