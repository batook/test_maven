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

class DeadlockRisk {
    private static class Resource {
        public int value;
    }

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
}

class ThreadA {
    public static void main(String[] args) {
        ThreadB b = new ThreadB();
        b.start();
        try {
            Thread.sleep(0);
            //Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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