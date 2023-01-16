package com.rasbet.backend.Entities;

import java.sql.SQLException;
import java.util.Collections;

import com.rasbet.backend.Database.NotificationDB;
import com.rasbet.backend.Interfaces.Observer;

public class FollowObserver implements Observer {
    private final int user_id;


    public FollowObserver(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    @Override
    public void update() {
        Notification n = new Notification(user_id, "TODO NOTIFCATION");
        try {
            NotificationDB.createAutomaticNotification(Collections.singletonList(n));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
