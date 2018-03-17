package com.batook.test;

import java.util.LinkedList;
import java.util.Random;

public class BlckQueue {
    private int threshold;
    private volatile int count;
    private LinkedList<String> queue = new LinkedList<>();

    BlckQueue() {
        this(10);
    }

    BlckQueue(int threshold) {
        this.threshold = threshold;
    }

    void offer(String e) throws InterruptedException {
        synchronized (this) {
            while (!(queue.size() < threshold)) {
                Thread.sleep(10);
                System.out.println(Thread.currentThread() + " Waiting...");
                wait();
            }
            if (queue.size() < threshold) {
                queue.offer(e);
                count++;
                System.out.println(Thread.currentThread() + " Offer " + e + " count=" + count);
            }
        }
    }

    String poll() throws InterruptedException {
        String e = null;
        synchronized (this) {
            if (count != 0) {
                notifyAll();
                e = queue.poll();
                count--;
                System.out.println(Thread.currentThread() + " Poll " + e + " count=" + count);
            }
            return e;
        }
    }

    public static void main(String[] args) {
        BlckQueue q = new BlckQueue();
        Random r = new Random();
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    q.offer(String.valueOf(r.nextInt(100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    q.poll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}

