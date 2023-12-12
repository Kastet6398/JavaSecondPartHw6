package com.example.demo.lib.models;

public class Project extends BaseModel {
    private final String name;
    private final User[] usersWithAccess;

    public String getName() {
        return name;
    }

    public User[] getUserWithAccess() {
        return usersWithAccess;
    }

    public Project(String name, User[] usersWithAccess) {
        this.name = name;
        this.usersWithAccess = usersWithAccess;
    }

    public Project(String name, User[] usersWithAccess, int id) {
        this.name = name;
        this.usersWithAccess = usersWithAccess;
        this.id = id;
    }
}
