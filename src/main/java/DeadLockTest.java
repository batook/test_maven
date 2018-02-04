public class DeadLockTest {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                try {
                    System.out.println("Lock A");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("Lock AB");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                try {
                    System.out.println("Lock B");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    System.out.println("Lock BA");
                }
            }
        }
        );
        t1.start();
        t2.start();
    }
}
