package com.batook.review;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class CASLock {
    private AtomicBoolean locked = new AtomicBoolean(false);

    public boolean lock() {
        return locked.compareAndSet(false, true);
    }

    public boolean unlock() {
        return locked.compareAndSet(true, false);
    }
}

class MyReentrantLock {
    boolean isLocked = false;
    Thread lockedBy = null;
    int lockedCount = 0;

    public synchronized void lock() throws InterruptedException {
        while (isLocked && lockedBy != Thread.currentThread()) {
            wait();
        }
        isLocked = true;
        lockedCount++;
        lockedBy = Thread.currentThread();
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            lockedCount--;
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }
}

class MyReadWriteLock {
    private int readers = 0;
    private int writers = 0;

    public synchronized void lockRead() throws InterruptedException {
        while (writers > 0) {
            wait();
        }
        readers++;
    }

    public synchronized void unlockRead() {
        readers--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        while (readers > 0 || writers > 0) {
            wait();
        }
        writers++;
    }

    public synchronized void unlockWrite() throws InterruptedException {
        writers--;
        notifyAll();
    }
}

class MyBlockingQueue {
    private List queue = new LinkedList();
    private int limit = 10;

    public MyBlockingQueue(int limit) {
        this.limit = limit;
    }

    public synchronized void put(Object item) throws InterruptedException {
        while (this.queue.size() == this.limit) {
            wait();
        }
        if (this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(item);
    }


    public synchronized Object take() throws InterruptedException {
        while (this.queue.size() == 0) {
            wait();
        }
        if (this.queue.size() == this.limit) {
            notifyAll();
        }
        return this.queue.remove(0);
    }
}