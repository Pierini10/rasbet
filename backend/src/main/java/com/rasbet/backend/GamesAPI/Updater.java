package com.rasbet.backend.GamesAPI;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import com.rasbet.backend.Database.EventsDB;
import com.rasbet.backend.Entities.SharedEventSubject;

@Component
public class Updater implements Runnable {

    private boolean running;
    /// Basic Reentrant Lock.
    private final ReentrantLock lock;
    /// Basic Condition.
    private final Condition condition;

    private LocalDateTime lastEventsUpdate;

    private boolean forceUpdate;

    private SharedEventSubject sharedEventSubject;

    private boolean can_update() {
        LocalDateTime now = LocalDateTime.now();
        if (lastEventsUpdate == null || lastEventsUpdate.isBefore(now.minusMinutes(5)) || this.forceUpdate) {
            return true;
        } else {
            return false;
        }
    }

    private void updatelasEventsUpdate() {
        lastEventsUpdate = LocalDateTime.now();
    }

    public void updateSharedEventSubject(SharedEventSubject sharedEventSubject) {
        this.sharedEventSubject =  sharedEventSubject;
    }

    public Updater() {
        this.running = true;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.sharedEventSubject = null;
    }

    @Override
    public void run() {
        while (running) {
            lock.lock();
            try {
                condition.await();
                update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private void update() throws Exception {
        if (this.can_update()) {
            if (sharedEventSubject != null) {
                EventsDB.update_Database(sharedEventSubject);
                updatelasEventsUpdate();
            }
            else {
                System.out.print("SharedEventSubject is null.");
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void signal(boolean forceUpdate) {
        if (!lock.isLocked()) {
            try {
                lock.lock();
                this.forceUpdate = forceUpdate;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

}
