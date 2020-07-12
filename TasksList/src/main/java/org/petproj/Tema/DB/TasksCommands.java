package org.petproj.Tema.DB;

import org.petproj.Tema.Structure.Task;

import javax.servlet.ServletException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TasksCommands {
    public static void updateTask(int userId, Task task) throws ServletException {
        String sqlRequest = "UPDATE tasks" + userId + " SET text = ?, description = ?, deadline = ? , state = ?, " +
                "target_id = ? WHERE task_id = ?";
        try (PreparedStatement preparedStatement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, task.getText());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, Date.valueOf(task.getDeadline()));
            preparedStatement.setBoolean(4, task.isState());
            preparedStatement.setInt(5, task.getTarget());
            preparedStatement.setInt(6, task.getId());
            System.out.print(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static void addTask(int userId, String description, String text, LocalDate deadline, boolean state) throws ServletException {
        String insert = "INSERT INTO tasks" + userId + " (description, text, deadline, state) VALUES ( ?, ?, ?, ?);";
        try (PreparedStatement statement = SingletonDBConnection.getDBConnection().prepareStatement(insert)) {
            statement.setString(1, description);
            statement.setString(2, text);
            statement.setDate(3, Date.valueOf(deadline));
            statement.setBoolean(4, state);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static boolean deleteTask(int userId, int taskId) throws ServletException {
        String sqlRequest = "DELETE from tasks" + userId + " WHERE task_id = ?;";
        try (PreparedStatement statement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            statement.setInt(1, taskId);
            return statement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static Task getTask(int user_id, int taskId) throws ServletException {
        String sqlRequest = "SELECT * FROM tasks" + user_id + " WHERE task_id = ?;";
        Task task = null;
        try (PreparedStatement preparedStatement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            preparedStatement.setInt(1, taskId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                String text = rs.getString("text");
                LocalDate deadline = rs.getDate("deadline").toLocalDate();
                boolean state = rs.getBoolean("state");
                int targetId = rs.getInt("target_id");
                task = new Task(taskId, description, text, deadline, state, targetId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return task;
    }

    public static List<Task> getTasks(int userId) throws ServletException {
        String sqlRequest = "SELECT * FROM tasks" + userId + ";";
        List<Task> list = new ArrayList<>();
        try (Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(sqlRequest);
            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String description = rs.getString("description");
                String text = rs.getString("text");
                LocalDate deadline = rs.getDate("deadline").toLocalDate();
                boolean state = rs.getBoolean("state");
                int targetId = rs.getInt("target_id");
                list.add(new Task(taskId, description, text, deadline, state, targetId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return list;
    }
}
