package com.example.demo.lib.fields;

import com.example.demo.lib.models.User;
import javafx.scene.control.CheckBox;

public class CustomCheckBox extends CheckBox {
    private final User user;

    public CustomCheckBox(User user) {
        this.user = user;
        setText(user.getLogin() + " <" + user.getEmail() + ">");
    }

    public User getUser() {
        return user;
    }
}
