package com.batook.test;

class DecoratorTest {
    public static void main(String[] args) {
        Component c = new Cocnag(new Sugar(new Milk(new BlackCoffee())));
        System.out.println(c.getDescription());
        Component c2 = new BlackCoffee().addComponent(new Milk()).addComponent(new Sugar());
        System.out.println(c2.getDescription());
        System.out.println(c2);
        ThreadProducer p = new ThreadProducer();
        new Thread(new ThreadConsumer(p)).start();
        new Thread(new ThreadConsumer(p)).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(p).start();
    }

}

class Component {
    Component component;
    String description;

    String getDescription() {
        return component.getDescription() + description;
    }

    Component addComponent(Component c) {
        c.component = this;
        return c;
    }
}

class BlackCoffee extends Component {

    public BlackCoffee() {
        description = "BlackCoffee";
    }

    @Override
    String getDescription() {
        return description;
    }
}

class Milk extends Component {
    public Milk() {
        description = "Milk";
    }

    public Milk(Component c) {
        this();
        component = c;
    }
}

class Sugar extends Component {
    public Sugar() {
        description = "Sugar";
    }

    public Sugar(Component c) {
        this();
        component = c;
    }
}

class Cocnag extends Component {
    public Cocnag() {
        description = "Cocnag";
    }

    public Cocnag(Component c) {
        this();
        component = c;
    }
}

class SingletonTest {
    private SingletonTest() {
    }

    public final static SingletonTest getInstance() {
        return Helper.Instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance());
        System.out.println(getInstance());
    }

    private static final class Helper {
        private final static SingletonTest Instance = new SingletonTest();
    }
}

class ThreadConsumer implements Runnable {
    ThreadProducer p;

    public ThreadConsumer(ThreadProducer p) {
        this.p = p;
    }

    @Override
    public void run() {
        synchronized (p) {
            if (!p.isDone) {
                try {
                    System.out.println(this + " is waiting for " + p);
                    p.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(this + " got result " + p.result);
        }
    }
}

class ThreadProducer implements Runnable {
    int result;
    volatile boolean isDone;

    @Override
    public void run() {
        synchronized (this) {
            result = 100;
            isDone = true;
            notifyAll();
        }

    }
}