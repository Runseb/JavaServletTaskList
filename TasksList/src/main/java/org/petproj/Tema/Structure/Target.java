package org.petproj.Tema.Structure;

import java.io.Serializable;

public class Target implements Serializable {
    private static final long SerialVersionUID = 1L;
    private int targetId;
    private String description;
    private int mainTargetId;

    public Target(int targetId, String description, int mainTargetId) {
        this.targetId = targetId;
        this.description = description;
        this.mainTargetId = mainTargetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMainTargetId() {
        return mainTargetId;
    }

    public void setMainTargetId(int mainTargetId) {
        this.mainTargetId = mainTargetId;
    }
}
