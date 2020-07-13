package org.petproj.Tema.Structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}