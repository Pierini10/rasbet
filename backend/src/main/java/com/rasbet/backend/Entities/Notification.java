package com.rasbet.backend.Entities;

public class Notification {
    private int idUser;
    private String description;

    public Notification(int idUser, String description) {
        this.idUser = idUser;
        this.description = description;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
