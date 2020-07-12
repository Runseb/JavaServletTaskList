package org.petproj.Tema.Structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private static final long SerialVersionUID = 1L;
    private String name;
    private String mail;
    private String password;
    private final List<Friend> friendsList = new ArrayList<>();
    private final List<Task> tasksList = new ArrayList<>();
    private final List<Target> targetList = new ArrayList<>();

    public User(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(password, user.password) &&
                Objects.equals(friendsList, user.friendsList) &&
                Objects.equals(tasksList, user.tasksList) &&
                Objects.equals(targetList, user.targetList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mail, password, friendsList, tasksList, targetList);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFriend(Friend friend) {
        friendsList.add(friend);
    }

    public void removeFriend(Friend friend) {
        friendsList.remove(friend);
    }

    public void addTask(Task task) {
        tasksList.add(task);
    }

    public void removeTask(Task task) {
        tasksList.remove(task);
    }

    public void addTarget(Target target) {
        targetList.add(target);
    }

    public void removeTarget(Target target) {
        targetList.remove(target);
    }

    public Target getTarget(int id) {
        return targetList.get(id);
    }

}