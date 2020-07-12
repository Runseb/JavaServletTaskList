package org.petproj.Tema.Structure;

import java.io.Serializable;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return targetId == target.targetId &&
                mainTargetId == target.mainTargetId &&
                Objects.equals(description, target.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId, description, mainTargetId);
    }

    @Override
    public String toString() {
        return "Target{" +
                "targetId=" + targetId +
                ", description='" + description + '\'' +
                ", mainTargetId=" + mainTargetId +
                '}';
    }


    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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
