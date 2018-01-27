import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadTests {
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
        Runnable r = new Tjoin();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t1.join();
        System.out.println("Main " + Thread.currentThread().getId());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getId());
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
        int[] data = new int[100_000_000];
        ForkJoinPool fjPool = new ForkJoinPool();
        RandomInitRA action = new RecursiveActionTest().new RandomInitRA(data, 0, data.length);
        fjPool.invoke(action);
        LocalTime t2 = LocalTime.now();
        System.out.println(Duration.between(t2, t1));
//        for (int i : data) {
//            System.out.println(i);
//        }
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