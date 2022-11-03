package com.rasbet.backend.Entities;

import java.util.List;

public class RequestNotifications {
    private int idRequestUser;
    private int idUser;
    private List<String> description;

    public RequestNotifications(int idRequestUser, int idUser, List<String> description) {
        this.idRequestUser = idRequestUser;
        this.idUser = idUser;
        this.description = description;
    }

    public int getIdRequestUser() {
        return this.idRequestUser;
    }

    public void setIdRequestUser(int idRequestUser) {
        this.idRequestUser = idRequestUser;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

}
