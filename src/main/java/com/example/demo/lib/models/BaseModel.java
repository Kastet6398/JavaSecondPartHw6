package com.example.demo.lib.models;

import java.lang.reflect.Field;

public class BaseModel {
    protected int id = -1;

    public int getId() {
        return id;
    }

    public String toString() {
        StringBuilder res = new StringBuilder(10000).append(getClass().getSimpleName()).append(" {");
        for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field classField = fields[i];
                classField.setAccessible(true);
                try {
                    res.append("\n    ").append(classField.getName()).append(" : ").append(classField.getType().getSimpleName()).append(" = ").append(classField.get(this));
                    if (i < fields.length - 1) res.append(",");
                } catch (IllegalAccessException ignored) {}
                classField.setAccessible(false);
            }
        }
        res.append("\n}");
        return res.toString();
    }
}
