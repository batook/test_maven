package com.batook.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class JoinForkTest {
    public static void main(String[] args) {
        JoinForkTest t = new JoinForkTest();
        ForkJoinPool pool = new ForkJoinPool(2);
        List<Integer> total = new ArrayList<>();
        pool.invoke(t.new RecursiveActionTest(total, 1, 10));
        System.out.println(total.stream().reduce((e1, e2) -> e1 + e2).get());
        System.out.println(total.stream().mapToInt(Integer::intValue).sum());
        Integer i = pool.invoke(t.new RecursiveTaskTest(1, 10));
        System.out.println(i);
        assert Integer.valueOf(55).equals(i);
    }

    class RecursiveActionTest extends RecursiveAction {
        int start;
        int end;
        int middle;
        List<Integer> total;

        public RecursiveActionTest(List<Integer> total, int start, int end) {
            this.start = start;
            this.end = end;
            this.total = total;
        }

        @Override
        protected void compute() {
            int result = 0;
            if (end - start <= 3) {
                for (int i = start; i <= end; i++)
                    result += i;
                total.add(result);
                System.out.println(Thread.currentThread().getName() + ": " + start + "->" + end + "=" + result);
            } else {
                middle = start + (end - start) / 2;
                RecursiveActionTest other = new RecursiveActionTest(total, start, middle);
                other.fork();
                new RecursiveActionTest(total, middle + 1, end).compute();
                other.join();
            }

        }
    }

    class RecursiveTaskTest extends RecursiveTask<Integer> {
        int start;
        int end;
        int middle;

        public RecursiveTaskTest(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int result = 0;
            if (end - start <= 5) {
                for (int i = start; i <= end; i++)
                    result += i;
                System.out.println(Thread.currentThread().getName() + ": " + start + "->" + end + "=" + result);
                return result;
            } else {
                middle = start + (end - start) / 2;
                RecursiveTaskTest other = new RecursiveTaskTest(start, middle);
                other.fork();
                return new RecursiveTaskTest(middle + 1, end).compute() + other.join();
            }
        }
    }
}

class CallableTest {
    public static void main(String[] args) {
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            Future<Long> r = service.submit(new CallableTest().new MyThread());
            System.out.println(r.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (service != null) service.shutdown();
        }
    }

    class MyThread implements Callable<Long> {
        @Override
        public Long call() throws Exception {
            long fact = 1;
            for (long i = 1; i <= 20; i++) {
                fact *= i;
            }
            return fact;
        }
    }
}

class BarrierTest {
    public static void main(String[] args) {
        BarrierTest b = new BarrierTest();
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(4);
            CyclicBarrier barrier = new CyclicBarrier(4, () -> System.out.println("All workers are here"));
            for (int i = 0; i < 4; i++)
                service.submit(b.new Worker(barrier));
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    class Worker implements Runnable {
        CyclicBarrier barrier;

        public Worker(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }
}

class InterruptedTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(i++);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                        return;
                    }
                }
            }

        });
        t.start();
        t.join(5000);
        t.interrupt();
    }
}


class WaitTest {
    Calc c;

    public static void main(String[] args) {
        WaitTest wt = new WaitTest();
        wt.c = wt.new Calc();
        new Thread(wt.new ReadCalc()).start();
        new Thread(wt.new ReadCalc()).start();
        new Thread(wt.c).start();
    }

    private class Calc implements Runnable {
        int total;
        volatile boolean isDone;

        public void run() {
            synchronized (this) {
                for (int i = 0; i < 100000; i++) {
                    total += i;
                }
                System.out.println("Calc is done...");
                isDone = true;
                notifyAll();
            }
        }
    }

    private class ReadCalc implements Runnable {
        @Override
        public void run() {
            synchronized (c) {
                while (!c.isDone) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting for Calc to complete...");
                        c.wait();
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println(Thread.currentThread().getName() + " Total is: " + c.total);
            }
        }
    }
}



