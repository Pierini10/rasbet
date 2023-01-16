package com.rasbet.backend.Entities;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Database.FollowDB;

@Component
public class SharedEventSubject implements FactoryBean<SharedEventSubject> {
    private static final Map<String, EventSubject> map = new ConcurrentHashMap<>();

    public SharedEventSubject(){
        List<String> event_ids = null;
        try {
            event_ids = EventsDB.getAllEventIds();
            for (String event_id : event_ids) {
                EventSubject es = new EventSubject(event_id);
                for (int user_id : FollowDB.getFollowers(event_id)) {
                    es.registerObserver(new FollowObserver(user_id));
                }
                map.put(event_id, es);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<?> getObjectType() {
        return EventSubject.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
    
    @Override
    @Nullable
    public SharedEventSubject getObject() throws Exception {
        return this;
    }

    public void addEvent(String event_id){
        map.put(event_id, new EventSubject(event_id));
    }

    public void addFollow(int user_id, String event_id){
        if (map.containsKey(event_id) && !map.get(event_id).isFollowed(user_id)) {
            map.get(event_id).registerObserver(new FollowObserver(user_id));
        }
        else {
            System.out.println("Event does not exist, probably ERROR");
        }
    }

    public void removeFollow(int user_id, String event_id){
        if (map.containsKey(event_id) && map.get(event_id).isFollowed(user_id)) {
            map.get(event_id).removeObserver(new FollowObserver(user_id));
        }
        else {
            System.out.println("Event does not exist, probably ERROR");
        }
    }

    public void notifyFollowers(String event_id, String message){
        if (map.containsKey(event_id)) {
            map.get(event_id).notifyObservers(message);
        }
        else {
            System.out.println("Event does not exist, probably ERROR");
        }
    }
}
