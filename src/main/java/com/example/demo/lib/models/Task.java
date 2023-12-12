package com.example.demo.lib.models;

public class Task extends BaseModel {
    private final String name;
    private final int projectId;
    private final User[] executors;

    public String getName() {
        return name;
    }

    public int getProjectId() {
        return projectId;
    }

    public Task(String name, int projectId, User[] executors) {
        this.name = name;
        this.projectId = projectId;
        this.executors = executors;
    }

    public Task(String name, int projectId, User[] executors, int id) {
        this.name = name;
        this.projectId = projectId;
        this.executors = executors;
        this.id = id;
    }

    public User[] getExecutors() {
        return executors;
    }
}
