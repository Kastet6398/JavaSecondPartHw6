package com.example.demo.lib;

import com.example.demo.lib.models.Comment;
import com.example.demo.lib.models.Project;
import com.example.demo.lib.models.Task;
import com.example.demo.lib.models.User;

import java.sql.*;

public class Dao {
    private final Connection connection;
    public Dao() throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.DB_URL, Settings.DB_USER, Settings.DB_PASSWORD);
        this.connection = connection;
        String querySQL = "CREATE TABLE IF NOT EXISTS %s (id SERIAL PRIMARY KEY, %s)";
        Statement statement = connection.createStatement();
        statement.execute(String.format(querySQL, "users", "login TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL"));
        statement.execute(String.format(querySQL, "projects", "name TEXT NOT NULL, userId INTEGER NOT NULL"));
        statement.execute(String.format(querySQL, "tasks", "name TEXT NOT NULL, projectId INTEGER NOT NULL"));
        statement.execute(String.format(querySQL, "comments", "\"text\" TEXT NOT NULL, taskId INTEGER NOT NULL"));
    }

    public void createProject(String project, int userId) {
        String createTableSQL = "INSERT INTO projects (name, userId) VALUES (?, ?);";

        try {
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.setString(1, project);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String login, String email, String password) {
        String createTableSQL = "INSERT INTO users (login, email, password) VALUES (?, ?, ?);";

        try {
            
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.setString(1, login);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTask(String name, int projectId) {
        String createTableSQL = "INSERT INTO tasks (name, projectId) VALUES (?, ?);";

        try {
            
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.setString(1, name);
            statement.setInt(2, projectId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createComment(String name, int taskId) {
        String createTableSQL = "INSERT INTO comments (text, taskId) VALUES (?, ?);";

        try {
            
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.setString(1, name);
            statement.setInt(2, taskId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Project[] fetchProjects(int userId) {
        String querySQL = "SELECT * FROM projects WHERE userId = ?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            Project[] projects = new Project[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                projects[i] = new Project(resultSet.getString("name"), resultSet.getInt("userId"), resultSet.getInt("id"));
            }
            return projects;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public User getUserByLogin(String login) {
        String querySQL = "SELECT * FROM users WHERE login=?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return new User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Task[] fetchTasks(int projectId) {
        String querySQL = "SELECT * FROM tasks WHERE projectId = ?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            Task[] tasks = new Task[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                tasks[i] = new Task(resultSet.getString("name"), resultSet.getInt("projectId"), resultSet.getInt("id"));
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Comment[] fetchComments(int taskId) {
        String querySQL = "SELECT * FROM comments WHERE taskId = ?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            Comment[] comments = new Comment[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                comments[i] = new Comment(resultSet.getString("text"), resultSet.getInt("taskId"), resultSet.getInt("id"));
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(int userId) {
        String querySQL = "SELECT * FROM users WHERE id=?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Project getProjectById(int projectId) {
        String querySQL = "SELECT * FROM projects WHERE id=?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL);
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new Project(resultSet.getString("name"), resultSet.getInt("userId"), resultSet.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Task getTaskById(int taskId) {
        String querySQL = "SELECT * FROM tasks WHERE id=?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL);
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new Task(resultSet.getString("name"), resultSet.getInt("projectId"), resultSet.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
