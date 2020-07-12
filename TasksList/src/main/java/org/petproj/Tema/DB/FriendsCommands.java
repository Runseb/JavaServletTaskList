package org.petproj.Tema.DB;

import org.petproj.Tema.Structure.Friend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendsCommands {
    /**
     * <code>addFriend</code> method for adding Friend in DB.
     * Method checked that friend exist in DB
     *
     * @param userId user ID
     * @param mail   Friend's email
     * @return successful of adding
     */
    public static boolean addFriend(int userId, String mail) throws ServletException {
        String select = "SELECT user_id, name FROM users WHERE mail = ? LIMIT 1;";
        String select1 = "SELECT name FROM friends" + userId + " WHERE friend_id = ? LIMIT 1;";
        String insert = "INSERT INTO friends" + userId + " (friend_id, name) VALUES (?,?);";
        boolean result = false;
        try (PreparedStatement selectFriends = SingletonDBConnection.getDBConnection().prepareStatement(select);
             PreparedStatement checkFriend = SingletonDBConnection.getDBConnection().prepareStatement(select1);
             PreparedStatement insertFriends = SingletonDBConnection.getDBConnection().prepareStatement(insert)) {
            selectFriends.setString(1, mail);
            ResultSet rs = selectFriends.executeQuery();
            int friendId = 0;
            String name = null;
            while (rs.next()) {
                friendId = rs.getInt("user_id");
                name = rs.getString("name");
            }
            if (userId != friendId) {
                checkFriend.setInt(1, friendId);
                ResultSet rs1 = checkFriend.executeQuery();
                if (!rs1.next()) {
                    insertFriends.setInt(1, friendId);
                    insertFriends.setString(2, name);
                    return (insertFriends.executeUpdate() > 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        //noinspection ConstantConditions
        return result;
    }

    /**
     * <code>addFriend</code> method for deleting friend from DB.
     *
     * @param userId   user ID
     * @param friendId Friend's ID
     * @return successful of deleting
     */
    public static boolean deleteFriend(int userId, int friendId) throws ServletException {
        String sqlDelete = "DELETE FROM friends" + userId + " WHERE friend_id = ?;";
        try (PreparedStatement deleteFriend = SingletonDBConnection.getDBConnection().prepareStatement(sqlDelete)) {
            deleteFriend.setInt(1, friendId);
            return deleteFriend.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static List<Friend> getFriends(int userId) throws ServletException {
        String sqlRequest = "SELECT * FROM friends" + userId + ";";
        List<Friend> friendList = new ArrayList<>();
        try (PreparedStatement statement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int friendId = rs.getInt("friend_id");
                String friendName = rs.getString("name");
                friendList.add(new Friend(friendName, friendId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return friendList;
    }

    public static void sendTaskToFriend(int userId, int taskId, int friendId) throws ServletException {
        String friendContainsInUserList = "SELECT name FROM friends" + userId + " WHERE friend_id = ? LIMIT 1;";
        String userContainInFriendList = "SELECT name FROM friends" + friendId + " WHERE friend_id = ? LIMIT 1;";
        String selectTaskInfo = "SELECT * FROM tasks" + userId + " WHERE task_id = ? LIMIT 1;";
        String sendTask = "INSERT INTO tasks" + friendId + " (description, text, deadline, state) VALUES (?, ?, ?, ?);";
        try (PreparedStatement st = SingletonDBConnection.getDBConnection().prepareStatement(friendContainsInUserList);
             PreparedStatement st1 = SingletonDBConnection.getDBConnection().prepareStatement(userContainInFriendList);
             PreparedStatement st2 = SingletonDBConnection.getDBConnection().prepareStatement(selectTaskInfo);
             PreparedStatement st3 = SingletonDBConnection.getDBConnection().prepareStatement(sendTask)) {
            st.setInt(1, friendId);
            st1.setInt(1, userId);
            st2.setInt(1, taskId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ResultSet rs1 = st1.executeQuery();
                if (rs1.next()) {
                    ResultSet rs2 = st2.executeQuery();
                    while (rs2.next()) {
                        st3.setString(1, rs2.getString("description"));
                        st3.setString(2, rs2.getString("text"));
                        st3.setDate(3, rs2.getDate("deadline"));
                        st3.setBoolean(4, rs2.getBoolean("state"));
                        st3.execute();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
