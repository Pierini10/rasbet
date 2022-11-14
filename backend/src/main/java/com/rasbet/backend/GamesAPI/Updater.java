package com.rasbet.backend.GamesAPI;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.rasbet.backend.Database.EventsDB;

public class Updater implements Runnable {
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_INTERVAL = 1;

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
            } finally {
                lock.unlock();
            }
        }
    }

    private void update() {
        if (this.can_update()) {
            int retries = 0;
            while (retries < MAX_RETRIES) {
                try {
                    EventsDB.update_Database();
                    updatelasEventsUpdate();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    retries++;
                    try {
                        Thread.sleep(RETRY_INTERVAL);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
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
