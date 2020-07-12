package org.petproj.Tema.DB;

import org.petproj.Tema.Structure.Target;

import javax.servlet.ServletException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TargetsCommands {
    public static void addTarget(int userId, String description) throws ServletException {
        String sqlRequest = "INSERT INTO targets" + userId + " (description) VALUES (?);";
        try (PreparedStatement preparedStatement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, description);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public static int updateTarget(int userId, Target target) throws ServletException {
        String select = "SELECT main_target_id FROM targets" + userId + " WHERE target_id = ? LIMIT 1;";
        String update = "UPDATE targets" + userId + " SET description = ?, main_target_id = ? WHERE target_id = ?";
        int result = 0;
        if (target.getTargetId() == target.getMainTargetId()) {
            target.setMainTargetId(0);
            result = 1;
        }
        try (PreparedStatement updateState = SingletonDBConnection.getDBConnection().prepareStatement(update);
             PreparedStatement selectState = SingletonDBConnection.getDBConnection().prepareStatement(select)) {
            if (target.getMainTargetId() != 0) {
                selectState.setInt(1, target.getTargetId());
                ResultSet rs = selectState.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("main_target_id") == target.getTargetId()) {
                        target.setMainTargetId(0);
                        result = 2;
                    }
                } else {
                    target.setMainTargetId(0);
                    result = 3;
                }
            }
            updateState.setString(1, target.getDescription());
            updateState.setInt(2, target.getMainTargetId());
            updateState.setInt(3, target.getTargetId());
            updateState.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return result;
    }

    public static List<Target> getTargets(int userId) throws ServletException {
        String sqlRequest = "SELECT * FROM targets" + userId + ";";
        List<Target> list = new ArrayList<>();
        try (Statement statement = SingletonDBConnection.getDBConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(sqlRequest);
            while (rs.next()) {
                int task_id = rs.getInt("target_id");
                String description = rs.getString("description");
                int mainTarget_id = rs.getInt("main_target_id");
                list.add(new Target(task_id, description, mainTarget_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return list;
    }

    public static Target getTarget(int userId, int target_id) throws ServletException {
        String sqlRequest = "SELECT * FROM targets" + userId + " WHERE target_id = ?;";
        Target target = null;
        try (PreparedStatement preparedStatement = SingletonDBConnection.getDBConnection().prepareStatement(sqlRequest)) {
            preparedStatement.setInt(1, target_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                int main_target_id = rs.getInt("main_target_id");
                target = new Target(target_id, description, main_target_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        return target;
    }

    public static void deleteTarget(int userId, int targetId) throws ServletException {
        String sqlSelect = "SELECT main_target_id FROM targets" + userId + " WHERE target_id = ?;";
        String sqlDelete = "DELETE from targets" + userId + " WHERE target_id = ?;";
        String sqlSelectTargets = "SELECT target_id FROM targets" + userId + " WHERE main_target_id = ?;";
        String sqlUpdateTargets = "UPDATE targets" + userId + " SET main_target_id = ? WHERE target_id = ?;";
        String sqlSelectTask = "SELECT task_id FROM tasks" + userId + " WHERE target_id = ?;";
        String sqlUpdateTasks = "UPDATE tasks" + userId + " SET target_id = ? WHERE task_id = ?;";
        int mainTargetId = 0;
        try (PreparedStatement statement = SingletonDBConnection.getDBConnection().prepareStatement(sqlSelect);
             PreparedStatement statement2 = SingletonDBConnection.getDBConnection().prepareStatement(sqlDelete);
             PreparedStatement statement3 = SingletonDBConnection.getDBConnection().prepareStatement(sqlSelectTargets);
             PreparedStatement statement4 = SingletonDBConnection.getDBConnection().prepareStatement(sqlUpdateTargets);
             PreparedStatement statement5 = SingletonDBConnection.getDBConnection().prepareStatement(sqlSelectTask);
             PreparedStatement statement6 = SingletonDBConnection.getDBConnection().prepareStatement(sqlUpdateTasks)) {
            statement.setInt(1, targetId);
            //take main target of deletion target
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                mainTargetId = rs.getInt("main_target_id");
            }
            //delete current target
            statement2.setInt(1, targetId);
            statement2.execute();
            //take all targets that has links on deleted target
            statement3.setInt(1, targetId);
            ResultSet rs3 = statement3.executeQuery();
            List<Integer> list = new ArrayList<>();
            while (rs3.next()) {
                list.add(rs3.getInt("target_id"));
            }
            //set new main target in all targets that had link on deleted target
            statement4.setInt(1, mainTargetId);
            for (int target : list) {
                statement4.setInt(2, target);
                statement4.execute();
            }
            //take all tasks that has links on deleted target
            statement5.setInt(1, targetId);
            ResultSet rs5 = statement5.executeQuery();
            list.clear();
            while (rs5.next()) {
                list.add(rs5.getInt("task_id"));
            }
            //change task's target link to main target
            statement6.setInt(1, mainTargetId);
            for (int task : list) {
                statement6.setInt(2, task);
                statement6.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
