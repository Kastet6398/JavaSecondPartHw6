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
        statement.execute(String.format(querySQL, "projects", "name TEXT NOT NULL"));
        statement.execute(String.format(querySQL, "tasks", "name TEXT NOT NULL, project_id INTEGER NOT NULL"));
        statement.execute(String.format(querySQL, "comments", "\"text\" TEXT NOT NULL, task_id INTEGER NOT NULL"));
        statement.execute(String.format(querySQL, "project_users", "project_id INTEGER, user_id INTEGER, FOREIGN KEY (project_id) REFERENCES projects(id), FOREIGN KEY (user_id) REFERENCES users(id)"));
        statement.execute(String.format(querySQL, "task_users", "task_id INTEGER, user_id INTEGER, FOREIGN KEY (task_id) REFERENCES tasks(id), FOREIGN KEY (user_id) REFERENCES users(id)"));
    }

    public void createProject(String project, User[] users) {
        String createTableSQL = "INSERT INTO projects (name) VALUES (?);";
        String createTableSQL2 = "INSERT INTO project_users (project_id, user_id) VALUES (?, ?);";

        try {
            PreparedStatement statement = connection.prepareStatement(createTableSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project);
            statement.executeUpdate();
            try {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int projectId = generatedKeys.getInt(1);
                    for (User i : users) {
                        PreparedStatement statement2 = connection.prepareStatement(createTableSQL2);
                        statement2.setInt(1, projectId);
                        statement2.setInt(2, i.getId());
                        statement2.executeUpdate();
                    }
                } else {
                    throw new SQLException("Creating project failed, no ID obtained.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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

    public void createTask(String name, int projectId, User[] executors) {
        String createTaskSQL = "INSERT INTO tasks (name, project_id) VALUES (?, ?)";
        String createTaskUserSQL = "INSERT INTO task_users (task_id, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(Settings.DB_URL, Settings.DB_USER, Settings.DB_PASSWORD);
             PreparedStatement createTaskStatement = connection.prepareStatement(createTaskSQL, Statement.RETURN_GENERATED_KEYS)) {

            createTaskStatement.setString(1, name);
            createTaskStatement.setInt(2, projectId);
            createTaskStatement.executeUpdate();

            try (ResultSet generatedKeys = createTaskStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int taskId = generatedKeys.getInt(1);
                    try (PreparedStatement createTaskUserStatement = connection.prepareStatement(createTaskUserSQL)) {
                        for (User executor : executors) {
                            createTaskUserStatement.setInt(1, taskId);
                            createTaskUserStatement.setInt(2, executor.getId());
                            createTaskUserStatement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void createComment(String name, int taskId) {
        String createTableSQL = "INSERT INTO comments (text, task_id) VALUES (?, ?);";

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
        String querySQL = """
        SELECT projects.id, projects.name
        FROM projects
        JOIN project_users ON projects.id = project_users.project_id
        WHERE project_users.user_id = ?""";

        try (PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.last();
                int rowCount = resultSet.getRow();
                resultSet.beforeFirst();
                Project[] projects = new Project[rowCount];
                for (int i = 0; resultSet.next(); i++) {
                    int projectId = resultSet.getInt("id");
                    String projectName = resultSet.getString("name");
                    User[] projectUsers = getProjectUsers(projectId);

                    projects[i] = new Project(projectName, projectUsers, projectId);
                }

                return projects;
            }
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
        String querySQL = "SELECT * FROM tasks WHERE project_id = ?";
        try {

            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            Task[] tasks = new Task[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                tasks[i] = new Task(resultSet.getString("name"), resultSet.getInt("project_id"), getTaskExecutors(resultSet.getInt("id")), resultSet.getInt("id"));
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User[] fetchUsers() {
        String querySQL = "SELECT * FROM users";
        try {

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(querySQL);
            resultSet.last();
            User[] tasks = new User[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                tasks[i] = new User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Comment[] fetchComments(int taskId) {
        String querySQL = "SELECT * FROM comments WHERE task_id = ?";
        try {
            
            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            Comment[] comments = new Comment[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++) {
                comments[i] = new Comment(resultSet.getString("text"), resultSet.getInt("task_id"), resultSet.getInt("id"));
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

    public User[] getProjectUsers(int projectId) {
        String querySQL = """
SELECT users.*
  FROM users
  JOIN project_users ON users.id = project_users.user_id
  WHERE project_users.project_id = ?;""";
        try {

            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            User[] users = new User[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++)
                users[i] = new User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User[] getTaskExecutors(int taskId) {
        String querySQL = """
SELECT users.*
  FROM users
  JOIN task_users ON users.id = task_users.user_id
  WHERE task_users.task_id = ?;""";
        try {

            PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.last();
            User[] users = new User[resultSet.getRow()];
            resultSet.beforeFirst();
            for (int i = 0; resultSet.next(); i++)
                users[i] = new User(resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getInt("id"));

            return users;
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
            return new Project(resultSet.getString("name"), getProjectUsers(projectId), resultSet.getInt("id"));
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
            return new Task(resultSet.getString("name"), resultSet.getInt("project_id"), getTaskExecutors(taskId), resultSet.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
