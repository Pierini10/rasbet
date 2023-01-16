package com.rasbet.backend.Entities;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rasbet.backend.Database.FollowDB;
import com.rasbet.backend.Interfaces.Observer;
import com.rasbet.backend.Interfaces.Subject;

public class EventSubject implements Subject{
    private final String event_id;
    private final List<FollowObserver> followers;

    public EventSubject(String event_id) {
        this.event_id = event_id;
        this.followers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        FollowObserver fo = (FollowObserver) o;
        followers.add(fo);
        try {
            FollowDB.followEvent(true, fo.getUser_id(), event_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeObserver(Observer o) {
        FollowObserver fo = (FollowObserver) o;
        followers.add(fo);
        try {
            FollowDB.followEvent(false, fo.getUser_id(), event_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers() {
        for (FollowObserver fo : followers) {
            fo.update();
        }
    }
}
