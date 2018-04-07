package com.batook.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreadAndConcurrencyTest {
}

class ThreadDemo {
    public static void main(String[] args) {
        boolean isDaemon = args.length != 0;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread thd = Thread.currentThread();
                while (!Thread.interrupted()) {
                    System.out.printf("%s is %salive and in %s " + "state%n", thd.getName(), thd.isAlive() ? "" : "not ", thd
                            .getState());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
            }
        };
        Thread t1 = new Thread(r, "thd1");
        if (isDaemon) t1.setDaemon(true);
        System.out.printf("%s is %salive and in %s state%n", t1.getName(), t1.isAlive() ? "" : "not ", t1.getState());
        Thread t2 = new Thread(r);
        t2.setName("thd2");
        if (isDaemon) t2.setDaemon(true);
        System.out.printf("%s is %salive and in %s state%n", t2.getName(), t2.isAlive() ? "" : "not ", t2.getState());
        t1.start();
        t2.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        t2.interrupt();
    }
}

class ProducerConsumerTest {
    public static void main(String[] args) {
        Shared s = new Shared();
        new Producer(s).start();
        new Consumer(s).start();
    }

    static class Shared {
        private char c;
        private volatile boolean writeable = true;

        synchronized char getSharedChar() {
            while (writeable) try {
                wait();
            } catch (InterruptedException ie) {
            }
            writeable = true;
            notify();
            System.out.println(c + " consumed by consumer.");
            return c;
        }

        synchronized void setSharedChar(char c) {
            while (!writeable) try {
                wait();
            } catch (InterruptedException ie) {
            }
            this.c = c;
            writeable = false;
            notify();
            System.out.println(c + " produced by producer.");
        }
    }

    static class Producer extends Thread {
        private final Shared s;

        Producer(Shared s) {
            this.s = s;
        }

        @Override
        public void run() {
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                s.setSharedChar(ch);
            }
        }
    }

    static class Consumer extends Thread {
        private final Shared s;

        Consumer(Shared s) {
            this.s = s;
        }

        @Override
        public void run() {
            char ch;
            do {
                ch = s.getSharedChar();
            } while (ch != 'Z');
        }
    }
}

class CountDownLatchDemo {
    final static int NTHREADS = 3;

    public static void main(String[] args) {
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(NTHREADS);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    report("entered run()");
                    startSignal.await(); // wait until told to ...
                    report("doing work"); // ... proceed
                    Thread.sleep((int) (Math.random() * 1000));
                    doneSignal.countDown(); // reduce count on which
                    // main thread is ...
                } // waiting
                catch (InterruptedException ie) {
                    System.err.println(ie);
                }
            }

            void report(String s) {
                System.out.println(System.currentTimeMillis() + ": " + Thread.currentThread() + ": " + s);
            }
        };
        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        for (int i = 0; i < NTHREADS; i++)
            executor.execute(r);
        try {
            System.out.println("main thread doing something");
            Thread.sleep(1000); // sleep for 1 second
            startSignal.countDown(); // let all threads proceed
            System.out.println("main thread doing something else");
            doneSignal.await(); // wait for all threads to finish
            executor.shutdownNow();
        } catch (InterruptedException ie) {
            System.err.println(ie);
        }
    }
}

class CyclicBarrierDemo {
    public static void main(String[] args) {
        float[][] matrix = new float[3][3];
        int counter = 0;
        for (int row = 0; row < matrix.length; row++)
            for (int col = 0; col < matrix[0].length; col++)
                matrix[row][col] = counter++;
        dump(matrix);
        System.out.println();
        Solver solver = new CyclicBarrierDemo().new Solver(matrix);
        System.out.println();
        dump(matrix);
    }

    static void dump(float[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++)
                System.out.print(matrix[row][col] + " ");
            System.out.println();
        }
    }

    class Solver {
        final int N;
        final float[][] data;
        final CyclicBarrier barrier;

        public Solver(float[][] matrix) {
            data = matrix;
            N = matrix.length;
            barrier = new CyclicBarrier(N, new Runnable() {
                @Override
                public void run() {
                    mergeRows();
                }
            });
            for (int i = 0; i < N; ++i)
                new Thread(new Worker(i)).start();
            waitUntilDone();
        }

        void mergeRows() {
            System.out.println("merging");
            synchronized ("abc") {
                "abc".notify();
            }
        }

        void waitUntilDone() {
            synchronized ("abc") {
                try {
                    System.out.println("main thread waiting");
                    "abc".wait();
                    System.out.println("main thread notified");
                } catch (InterruptedException ie) {
                    System.out.println("main thread interrupted");
                }
            }
        }

        class Worker implements Runnable {
            int myRow;
            boolean done = false;

            Worker(int row) {
                myRow = row;
            }

            boolean done() {
                return done;
            }

            void processRow(int myRow) {
                System.out.println("Processing row: " + myRow);
                for (int i = 0; i < N; i++)
                    data[myRow][i] *= 10;
                done = true;
            }

            @Override
            public void run() {
                while (!done()) {
                    processRow(myRow);
                    try {
                        barrier.await();
                    } catch (InterruptedException ie) {
                        return;
                    } catch (BrokenBarrierException bbe) {
                        return;
                    }
                }
            }
        }
    }
}

class PhaserDemo {
    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> System.out.printf("%s running at %d%n", Thread.currentThread().getName(), System.currentTimeMillis()));
        tasks.add(() -> System.out.printf("%s running at %d%n", Thread.currentThread().getName(), System.currentTimeMillis()));
        runTasks(tasks);
    }

    static void runTasks(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1); // "1" (register self)
        // create and start threads
        for (final Runnable task : tasks) {
            phaser.register();
            Runnable r = () -> {
                try {
                    Thread.sleep(50 + (int) (Math.random() * 300));
                } catch (InterruptedException ie) {
                    System.out.println("interrupted thread");
                }
                phaser.arriveAndAwaitAdvance(); // await the ...
                // creation of ...
                // all tasks
                task.run();
            };
            Executors.newSingleThreadExecutor().execute(r);
        }
        // allow threads to start and deregister self
        phaser.arriveAndDeregister();
    }
}

class RLDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final ReentrantLock lock = new ReentrantLock();
        class Worker implements Runnable {
            private final String name;

            Worker(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                lock.lock();
                try {
                    if (lock.isHeldByCurrentThread()) System.out.printf("Thread %s entered critical section.%n", name);
                    System.out.printf("Thread %s performing work.%n", name);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.printf("Thread %s finished working.%n", name);
                } finally {
                    lock.unlock();
                }
            }
        }
        executor.execute(new Worker("ThdA"));
        executor.execute(new Worker("ThdB"));
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println("shutdown...");
        executor.shutdownNow();
    }
}

class LockTest {
    Calc c;

    public static void main(String[] args) {
        LockTest lt = new LockTest();
        lt.c = lt.new Calc();
        new Thread(lt.new ReadCalc()).start();
        new Thread(lt.new ReadCalc()).start();
        new Thread(lt.c).start();
    }

    private class Calc implements Runnable {
        private final Lock lock;
        private final Condition condition;
        int total;
        private volatile boolean isDone;

        public Calc() {
            isDone = false;
            lock = new ReentrantLock();
            condition = lock.newCondition();
        }

        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 100000; i++) {
                    for (int j = 0; j < 1000; j++) {
                        System.nanoTime();
                    }
                    total += i;
                }
                System.out.println("Calc is done...");
                isDone = true;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    private class ReadCalc implements Runnable {
        @Override
        public void run() {
            c.lock.lock();
            try {
                while (!c.isDone) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting for Calc to complete...");
                        c.condition.await();
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println(Thread.currentThread().getName() + " Total is: " + c.total);
            } finally {
                c.lock.unlock();
            }
        }
    }
}

class Dictionary {
    public static void main(String[] args) {
        final String[] words = {"hypocalcemia", "prolixity", "assiduous", "indefatigable", "castellan"};
        final String[] definitions = {"a deficiency of calcium in the blood", "unduly prolonged or drawn out", "showing great care, attention, and effort", "able to work or continue for a lengthy time without tiring", "the govenor or warden of a castle or fort"};
        final Map<String, String> dictionary = new HashMap<String, String>();
        ReadWriteLock rwl = new ReentrantReadWriteLock(true);
        final Lock rlock = rwl.readLock();
        final Lock wlock = rwl.writeLock();
        Runnable writer = () -> {
            for (int i = 0; i < words.length; i++) {
                wlock.lock();
                try {
                    dictionary.put(words[i], definitions[i]);
                    System.out.println("writer storing " + words[i] + " entry");
                } finally {
                    wlock.unlock();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    System.err.println("writer " + "interrupted");
                }
            }
        };
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(writer);
        Runnable reader = () -> {
            while (true) {
                rlock.lock();
                try {
                    int i = (int) (Math.random() * words.length);
                    System.out.println("reader accessing " + words[i] + ": " + dictionary.get(words[i]) + " entry");
                } finally {
                    rlock.unlock();
                }
            }
        };
        es = Executors.newFixedThreadPool(1);
        es.submit(reader);
    }
}

class CSDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletionService<BigDecimal> cs = new ExecutorCompletionService<BigDecimal>(es);
        cs.submit(new CalculateE(17));
        cs.submit(new CalculateE(170));
        Future<BigDecimal> result = cs.take();
        System.out.println(result.get());
        System.out.println();
        result = cs.take();
        System.out.println(result.get());
        es.shutdown();
    }
}

class CalculateE implements Callable<BigDecimal> {
    final int lastIter;

    public CalculateE(int lastIter) {
        this.lastIter = lastIter;
    }

    @Override
    public BigDecimal call() {
        MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i <= lastIter; i++) {
            BigDecimal factorial = factorial(new BigDecimal(i));
            BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
            result = result.add(res);
        }
        return result;
    }

    private BigDecimal factorial(BigDecimal n) {
        if (n.equals(BigDecimal.ZERO)) return BigDecimal.ONE;
        else return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
    }
}
