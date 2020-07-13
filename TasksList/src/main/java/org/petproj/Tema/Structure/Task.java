package org.petproj.Tema.Structure;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long SerialVersionUID = 1L;
    private int id;
    private String description;
    private String text;
    private LocalDate deadline;
    private boolean state = false;
    private int targetID;

    public Task(int id, String description, String text, LocalDate deadline, boolean state, int targetID) {
        this.id = id;
        this.description = description;
        this.text = text;
        this.deadline = deadline;
        this.state = state;
        this.targetID = targetID;
    }

    public int getId() {
        return id;
    }

    public int getTarget() {
        return targetID;
    }

    public void setTarget(int targetID) {
        this.targetID = targetID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
