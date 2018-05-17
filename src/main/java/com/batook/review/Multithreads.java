package com.batook.review;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class CallableVsRunnable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService s = Executors.newSingleThreadExecutor();
        Future f1 = s.submit(() -> {
            Thread.sleep(10);
            return 1;
        });
        Future f2 = s.submit(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(f1.get() + " " + f2.get());
    }
}

class Deadlock {
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

class InterruptedTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread()
                              .isInterrupted()) {
                    System.out.println(Thread.currentThread() + " is working");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread() + " interrupted");
                        return;
                    }
                }
            }
        });
        t.start();
        t.join(4000);
        t.interrupt();
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
        AtomicInteger i = new AtomicInteger(0);

        @Override
        public void run() {
            try {
                System.out.println(" Producer is working");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                i.set(ThreadLocalRandom.current()
                                       .nextInt());
                System.out.println(" Producer " + this + " i=" + i.get());
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
                System.out.println(Thread.currentThread() + " i=" + p.i.get());
            }
        }
    }
}