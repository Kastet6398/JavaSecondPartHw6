package com.example.demo.lib.models;

public class Comment extends BaseModel {
    private final String text;
    private final int taskId;

    public String getText() {
        return text;
    }

    public int getTaskId() {
        return taskId;
    }

    public Comment(String text, int taskId) {
        this.text = text;
        this.taskId = taskId;
    }

    public Comment(String name, int projectId, int id) {
        this.text = name;
        this.taskId = projectId;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Comment(" +
                "text=\"" + text + '"' +
                ", taskId=\"" + taskId + '"' +
                ')';
    }
}
