package org.petproj.Tema.DB;

import org.petproj.Tema.Structure.User;

import javax.servlet.ServletException;
import java.sql.*;

public class OtherCommands {

    public static void createUsersTable() throws ServletException {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (USER_ID SERIAL PRIMARY KEY, session_id VARCHAR(100), " +
                "NAME VARCHAR(100) NOT NULL, mail VARCHAR(100) NOT NULL, password VARCHAR(100) NOT NULL, " +
                "FRIENDS VARCHAR(100), TASKS VARCHAR(100), TARGETS VARCHAR(100));";
        try (PreparedStatement statement = SingletonDBConnection.getDBConnection().prepareStatement(usersTable)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static int addUser(User user) throws ServletException {
        String addUser = "INSERT INTO users (name, mail, password) VALUES ( ? , ? , ?) RETURNING user_id;";
        int userID = 0;
        try (PreparedStatement addNewUser = SingletonDBConnection.getDBConnection().prepareStatement(addUser);
             Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            addNewUser.setString(1, user.getName());
            addNewUser.setString(2, user.getMail());
            addNewUser.setString(3, user.getPassword());
            ResultSet rs = addNewUser.executeQuery();  //User ID  will returned
            while (rs.next()) {
                userID = rs.getInt("user_id");
            }
            String userFriendTable = "CREATE TABLE friends" + userID + " (friend_id INT PRIMARY KEY NOT NULL, name VARCHAR(200) NOT NULL);";
            String userTasksTable = "CREATE TABLE tasks" + userID + " (task_id SERIAL PRIMARY KEY, " +
                    "description VARCHAR(100) NOT NULL, text VARCHAR(1000) NOT NULL, " +
                    "deadline DATE NOT NULL, state BOOLEAN NOT NULL, target_id INT);";
            String userTargetsTable = "CREATE TABLE targets" + userID + " (target_id SERIAL PRIMARY KEY, description " +
                    " VARCHAR(200) NOT NULL, main_target_id INT);";
            String updateTablesInUser = "UPDATE users SET TASKS = 'tasks" + userID + "', TARGETS = 'targets" +
                    userID + "', FRIENDS = 'friends" + userID + "' WHERE USER_ID = " + userID + ";";
            statement.execute(userFriendTable + userTasksTable + userTargetsTable + updateTablesInUser);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return userID;
    }

    public static void updateSessionId(int userId, String sessionId) throws ServletException {
        String sqlRequest = "UPDATE users SET session_id = ? WHERE user_id = ?";
        try (PreparedStatement preparedStatement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, sessionId);
            preparedStatement.setInt(2, userId);
            System.out.println(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static int checkUserSessionId(String sessionId) throws ServletException {
        String sqlRequest = "SELECT user_id FROM users WHERE session_id = '" + sessionId + "';";
        int userId = 0;
        try (Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            System.out.println(sqlRequest);
            ResultSet rs = statement.executeQuery(sqlRequest);
            if (rs.next()) userId = rs.getInt("user_id");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return userId;
    }

    public static int checkUser(String mail, String password) throws ServletException {
        String sqlRequest = "SELECT user_id FROM users WHERE mail = '" + mail + "' AND password = '" + password + "';";
        int userId = 0;
        try (Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            System.out.println(sqlRequest);
            ResultSet rs = statement.executeQuery(sqlRequest);
            if (rs.next()) userId = rs.getInt("user_id");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return userId;
    }

    public static boolean checkUserContains(String mail) throws ServletException {
        String sqlRequest = "SELECT user_id FROM users WHERE mail = '" + mail + "';";
        boolean contains = false;
        try (Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            System.out.println(sqlRequest);
            ResultSet rs = statement.executeQuery(sqlRequest);
            contains = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return contains;
    }
}
