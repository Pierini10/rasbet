package com.rasbet.backend.GamesAPI;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.rasbet.backend.Database.EventsDB;

public class Updater implements Runnable {

    private boolean running;
    /// Basic Reentrant Lock.
    private final ReentrantLock lock;
    /// Basic Condition.
    private final Condition condition;

    private LocalDateTime lastEventsUpdate;

    private boolean forceUpdate;

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

    public Updater() {
        this.running = true;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
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
            EventsDB.update_Database();
            updatelasEventsUpdate();
            // TODO POINT OF NOTIFICATION
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
