package com.batook.test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class ThreadTest {
    public static void main(String[] args) {
        DeadlockRisk dl = new DeadlockRisk();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                dl.read();
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                dl.write(1, 1);
            }
        });
        a.start();
        b.start();
    }
}

class CalcTest {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        new Reader(calculator).start();
        new Reader(calculator).start();
        new Reader(calculator).start();
        new Thread(calculator).start();
    }
}

class Reader extends Thread {
    Calculator c;

    public Reader(Calculator calc) {
        c = calc;
    }

    public void run() {
        synchronized (c) {
            // wait until Calc result is available
            if (c.isDone) {
                System.out.println(Thread.currentThread().getName() + " No need to wait. Calc is done");
            } else {
                try {
                    System.out.println(Thread.currentThread().getName() + " Waiting for calculation...");
                    c.wait();
                } catch (InterruptedException e) {
                }
            }
            System.out.println(Thread.currentThread().getName() + " Total is: " + c.total);
        }
    }
}

class Calculator implements Runnable {
    int total;
    boolean isDone;

    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start...");
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                total += i;
            }
            notifyAll();
            isDone = true;
            System.out.println(Thread.currentThread().getName() + " Done...");
        }
    }
}

class DeadlockRisk {
    private Resource resourceA = new Resource();
    private Resource resourceB = new Resource();

    public int read() {
        synchronized (resourceA) { // May deadlock here
            System.out.println("Lock rA");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resourceB) {
                System.out.println("Lock rB");
                return resourceB.value + resourceA.value;
            }
        }
    }

    public void write(int a, int b) {
        synchronized (resourceB) { // May deadlock here
            System.out.println("Lock wB");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resourceA) {
                System.out.println("Lock wA");
                resourceA.value = a;
                resourceB.value = b;
            }
        }
    }

    private static class Resource {
        public int value;
    }
}

class ThreadA {
    public static void main(String[] args) {
        ThreadB b = new ThreadB();
        b.start();
        synchronized (b) {
            System.out.println("ThreadA");
            try {
                System.out.println("Waiting for b to complete...");
                b.wait();
            } catch (InterruptedException e) {
            }
            System.out.println("Total is: " + b.total);
        }
    }
}

class ThreadB extends Thread {
    int total;

    public void run() {
        synchronized (this) {
            System.out.println("ThreadB");
            for (int i = 0; i < 100; i++) {
                total += i;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Calc is done...");
            notify();
        }
    }
}

class Chess implements Runnable {
    public static void main(String[] args) {
        Chess ch = new Chess();
        new Thread(ch).start();
        new Thread(new Chess()).start();
    }

    @Override
    public void run() {
        move(Thread.currentThread().getId());
    }

    synchronized void move(long id) {
        System.out.print(id + " ");
        System.out.print(id + " ");
    }
}

class Tjoin implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Tjoin());
        t1.start();
        t1.join();
        System.out.println("Main " + Thread.currentThread().getName());
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Tlistarray implements Runnable {
    List<Integer> list = new CopyOnWriteArrayList<>();

    public Tlistarray() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Runnable ar = new Tlistarray();
        Thread a = new Thread(ar);
        Thread b = new Thread(ar);
        a.start();
        b.start();

    }

    @Override
    public void run() {
        String tName = Thread.currentThread().getName();
        while (!list.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tName + " remove " + list.remove(0));
        }
    }
}

class RecursiveActionTest {
    public static void main(String[] args) {
        LocalTime t1 = LocalTime.now();
        int[] data = new int[1_000_000];
        ForkJoinPool fjPool = new ForkJoinPool();
        RandomInitRA action = new RecursiveActionTest().new RandomInitRA(data, 0, data.length);
        fjPool.invoke(action);
        LocalTime t2 = LocalTime.now();
        System.out.println(Duration.between(t2, t1));
    }

    class RandomInitRA extends RecursiveAction {
        private static final int THRESHOLD = 10000;
        private int[] data;
        private int start;
        private int end;

        RandomInitRA(int[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < 1000; j++) {
                        ThreadLocalRandom.current().nextInt(1, 10);
                    }
                    data[i] = ThreadLocalRandom.current().nextInt(1, 100);
                }
            } else {
                int halfWay = ((end - start) / 2) + start;
                RandomInitRA a1 = new RandomInitRA(data, start, halfWay);
                a1.fork();
                RandomInitRA a2 = new RandomInitRA(data, halfWay, end);
                a2.compute();
                a1.join();
            }
        }
    }
}

class RecursiveTaskTest {
    static final int NUM_THREADS = 1;
    static final int N = 1000_000_000;

    public static void main(String[] args) {
        long result;
        ForkJoinPool fjPool = new ForkJoinPool(NUM_THREADS);
        RecursiveTask<Long> task = new RecursiveTaskTest().new RTask(0, N);
        result = fjPool.invoke(task);
        System.out.println(result);
        assert result == (long) N * (N + 1) / 2 : result;
        assertEquals((long) N * (N + 1) / 2, result);
    }

    class RTask extends RecursiveTask<Long> {
        private long from;
        private long to;


        public RTask(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            long result = 0;
            if (to - from <= N / NUM_THREADS) {
                for (long i = from; i <= to; i++) {
                    result += i;
                }
                return result;
            } else {
                long mid = (to + from) / 2;
                RTask firstHalf = new RTask(from, mid);
                firstHalf.fork();
                RTask secondHalf = new RTask(mid + 1, to);
                long secondResult = secondHalf.compute();
                long firstResult = firstHalf.join();
                return firstResult + secondResult;
            }
        }
    }

}