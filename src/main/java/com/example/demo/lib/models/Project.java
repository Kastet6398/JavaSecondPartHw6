package com.example.demo.lib.models;

public class Project extends BaseModel {
    private final String name;
    private final int userId;

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public Project(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public Project(String name, int projectId, int id) {
        this.name = name;
        this.userId = projectId;
        this.id = id;
    }
}
