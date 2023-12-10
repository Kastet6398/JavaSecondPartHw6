package com.example.demo.lib.models;

public class Task extends BaseModel {
    private final String name;
    private final int projectId;

    public String getName() {
        return name;
    }

    public int getProjectId() {
        return projectId;
    }

    public Task(String name, int projectId) {
        this.name = name;
        this.projectId = projectId;
    }

    public Task(String name, int projectId, int id) {
        this.name = name;
        this.projectId = projectId;
        this.id = id;
    }
}
