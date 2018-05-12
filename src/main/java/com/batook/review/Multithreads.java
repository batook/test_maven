package com.batook.review;

public class Multithreads {
}

class DeadlockTest {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B) {
                        System.out.println("B");
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A) {
                        System.out.println("A");
                    }
                }
            }
        }).start();
    }
}

class Interleave {
    public static void main(String[] args) {
        Printer p = new Printer();
        new Thread(new Even(p)).start();
        new Thread(new Odd(p)).start();
    }

    static class Printer {
        static volatile boolean isEven = true;

        synchronized void printEven(String s) {
            while (!isEven) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notify();
            isEven = false;
            System.out.println(s);
        }

        synchronized void printOdd(String s) {
            while (isEven) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notify();
            isEven = true;
            System.out.println(s);
        }

    }

    static class Even implements Runnable {
        Printer p;

        Even(Printer p) {
            this.p = p;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) p.printEven(Thread.currentThread() + " " + i);
            }
        }
    }

    static class Odd implements Runnable {
        Printer p;

        Odd(Printer p) {
            this.p = p;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (i % 2 != 0) p.printOdd(Thread.currentThread() + " " + i);
            }
        }
    }
}

class ProducerConsumer {
    public static void main(String[] args) {
        Producer prod = new Producer();
        new Thread(new Consumer(prod)).start();
        new Thread(new Consumer(prod)).start();
        new Thread(new Consumer(prod)).start();
        new Thread(prod).start();
    }

    static class Producer implements Runnable {
        volatile boolean isReady;

        @Override
        public void run() {
            try {
                System.out.println(" Producer is working");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                System.out.println(" Producer " + this + " is ready");
                isReady = true;
                notifyAll();
            }

        }
    }

    static class Consumer implements Runnable {
        Producer p;

        Consumer(Producer p) {
            this.p = p;
        }

        @Override
        public void run() {
            synchronized (p) {
                while (!p.isReady) {
                    try {
                        System.out.println(Thread.currentThread() + " is waiting for " + p);
                        p.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread() + " done");
            }
        }
    }
}