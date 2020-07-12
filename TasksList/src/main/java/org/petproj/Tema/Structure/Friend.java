package org.petproj.Tema.Structure;

import java.io.Serializable;
import java.util.Objects;

public class Friend implements Serializable {
    private static final long SerialVersionUID = 1L;
    private int id;
    private String name;


    public Friend(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return id == friend.id &&
                Objects.equals(name, friend.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
