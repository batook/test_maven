package com.batook.review;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.stream.IntStream;


class ReadWriteLockTest {
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static ArrayList<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            if (i % 5 == 0) {
                service.execute(new Writer());
            } else {
                service.execute(new Reader());
            }
        }
        service.shutdown();
    }

    static class Reader implements Runnable {
        Lock readLock = lock.readLock();

        @Override
        public void run() {
            readLock.lock();
            try {
                Thread.sleep(100);
                System.out.println(" Read " + list);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            readLock.unlock();
        }
    }

    static class Writer implements Runnable {
        Lock writeLock = lock.writeLock();

        @Override
        public void run() {
            writeLock.lock();
            try {
                Thread.sleep(100);
                int i = new Random().nextInt();
                list.add(i);
                System.out.println(" Write " + i);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            writeLock.unlock();
        }
    }
}

class CPLock {
    Producer producer;

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CPLock c = new CPLock();
        c.producer = c.new Producer();
        service.execute(c.new Consumer());
        service.execute(c.new Consumer());
        service.execute(c.new Consumer());
        service.execute(c.producer);
        service.shutdown();
    }

    class Producer implements Runnable {
        ReentrantLock pLock = new ReentrantLock();
        Condition ready = pLock.newCondition();
        volatile boolean isReady = false;

        @Override
        public void run() {
            pLock.lock();
            System.out.println("Producer is working");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Done");
            isReady = true;
            ready.signalAll();
            pLock.unlock();
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            producer.pLock.lock();
            while (!producer.isReady) {
                try {
                    System.out.println(Thread.currentThread() + " is waiting for " + producer);
                    producer.ready.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " consumed");
            }
            producer.pLock.unlock();
        }
    }
}

class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();
        Runnable r = () -> {
            lock.lock();
            if (lock.isHeldByCurrentThread()) System.out.println("Lock is aquired by " + Thread.currentThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        };
        service.execute(r);
        service.execute(r);
        service.awaitTermination(5, TimeUnit.SECONDS);
        service.shutdown();
    }
}

class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new Waiter(latch));
        service.execute(new Decrementer(latch));
        service.shutdown();
    }

    static class Waiter implements Runnable {
        CountDownLatch latch;

        Waiter(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println("Waiter is waiting...");
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waiter released");
        }
    }

    static class Decrementer implements Runnable {
        CountDownLatch latch;

        Decrementer(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                latch.countDown();
                System.out.println("3");
                Thread.sleep(1000);
                latch.countDown();
                System.out.println("2");
                Thread.sleep(1000);
                latch.countDown();
                System.out.println("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("barrier passed"));
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 8; i++) {
            service.submit(new Worker(barrier));
        }
        service.shutdown();
    }

    static class Worker implements Runnable {
        CyclicBarrier barrier;

        Worker(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread() + " waiting");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}

class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> result = service.submit(() -> IntStream.range(1, 100)
                                                               .sum());
        System.out.println(result.get());
        service.shutdown();
    }
}

class FibonacciTask extends RecursiveTask<Long> {
    private Long n;

    FibonacciTask(Long n) {
        this.n = n;
    }

    public static void main(String[] args) {
        FibonacciTask task = new FibonacciTask(10L);
        ForkJoinPool pool = new ForkJoinPool();
        Long i = pool.invoke(task);
        System.out.println(i);
    }

    @Override
    protected Long compute() {
        if (n == 1 || n == 2) return 1L;
        else {
            FibonacciTask f1 = new FibonacciTask(n - 1);
            f1.fork();
            FibonacciTask f2 = new FibonacciTask(n - 2);
            return f2.compute() + f1.join();
        }
    }
}

class MergeSortTask extends RecursiveAction {
    private int[] tmp;

    MergeSortTask(int[] unsorted) {
        this.tmp = unsorted;
    }

    public static void main(String[] args) {
        final int[] unsorted = new Random().ints(-10, 100)
                                           .limit(1_00)
                                           .toArray();
        ForkJoinPool pool = new ForkJoinPool();
        MergeSortTask task = new MergeSortTask(unsorted.clone());
        //
        long startTime = System.nanoTime();
        task.sort(unsorted.clone());
        double estimatedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println("Serial " + new DecimalFormat("#.##########").format(estimatedTime));
        //
        startTime = System.nanoTime();
        pool.invoke(task);
        estimatedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println("Parallel " + new DecimalFormat("#.##########").format(estimatedTime));
    }

    @Override
    protected void compute() {
        if (tmp.length > 1) {
            int[] left = new int[tmp.length / 2];
            int[] right = new int[tmp.length - tmp.length / 2];
            System.arraycopy(tmp, 0, left, 0, left.length);
            System.arraycopy(tmp, left.length, right, 0, right.length);
            invokeAll(new MergeSortTask(left), new MergeSortTask(right));
            merge(tmp, left, right);
            System.out.println(Thread.currentThread() + " " + Arrays.toString(tmp));
        }
    }

    void sort(int[] unsorted) {
        if (unsorted.length > 1) {
            int[] left = new int[unsorted.length / 2];
            int[] right = new int[unsorted.length - unsorted.length / 2];
            System.arraycopy(unsorted, 0, left, 0, left.length);
            System.arraycopy(unsorted, left.length, right, 0, right.length);
            sort(left);
            sort(right);
            merge(unsorted, left, right);
        }
    }

    void merge(int[] result, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) result[k++] = left[i++];
        while (j < right.length) result[k++] = right[j++];
    }

}