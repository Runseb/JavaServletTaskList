package org.petproj.Tema.Structure;

import java.io.Serializable;

public class Friend implements Serializable {
    private static final long SerialVersionUID = 1L;
    private int id;
    private String name;


    public Friend(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
