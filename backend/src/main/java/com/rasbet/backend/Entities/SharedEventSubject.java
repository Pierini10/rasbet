package com.rasbet.backend.Entities;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import com.rasbet.backend.Database.EventsDB;

public class SharedEventSubject implements FactoryBean<Map<String, EventSubject>> {
    private final Map<String, EventSubject> map = new ConcurrentHashMap<>();

    public SharedEventSubject(){
        List<String> event_ids = null;
        try {
            event_ids = EventsDB.getAllEventIds();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String event_id : event_ids) {
            map.put(event_id, new EventSubject(event_id));
        }
    }

    public void addEvent(String event_id){
        map.put(event_id, new EventSubject(event_id));
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
    public Map<String, EventSubject> getObject() throws Exception {
        return null;
    }
}
