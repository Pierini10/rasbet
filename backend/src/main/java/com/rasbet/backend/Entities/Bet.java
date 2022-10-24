package com.rasbet.backend.Entities;

import java.time.LocalDateTime;

public class Bet {
    private Integer id;
    private Integer idUser;
    private Integer idBetState;
    private Integer amount;
    private LocalDateTime dateTime;

    public Bet(Integer id, Integer idUser, Integer idBetState, Integer amount, LocalDateTime dateTime){
        this.id = id;
        this.idUser = idUser;
        this.idBetState = idBetState;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    //basic getters
    public Integer getId() {
        return id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdBetState() {
        return idBetState;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    //basic setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setIdBetState(Integer idBetState) {
        this.idBetState = idBetState;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void update_info(Integer id, Integer idUser, Integer idBetState, Integer amount, LocalDateTime dateTime) {
        if (id != null) this.id = id;
        if (idUser != null) this.idUser = idUser; 
        if (idBetState != null) this.idBetState = idBetState; 
        if (amount != null) this.amount = amount; 
        if (dateTime != null) this.dateTime = dateTime;
    }
}
