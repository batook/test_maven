class ConcurrencyTest {
    public static void main(String[] args) {

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
    public static void main(String[] args) {
        Calc c = new WaitTest().new Calc();
        new Thread(new WaitTest().new ReadCalc(c)).start();
        new Thread(new WaitTest().new ReadCalc(c)).start();
        new Thread(c).start();
    }

    class Calc implements Runnable {
        int total;
        boolean isDone;

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

    class ReadCalc implements Runnable {
        Calc c;

        public ReadCalc(Calc c) {
            this.c = c;
        }

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



