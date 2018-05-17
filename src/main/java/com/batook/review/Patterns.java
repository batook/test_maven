package com.batook.review;

import java.util.ArrayList;


class SingletonTest {
    private SingletonTest() {
    }

    public static void main(String[] args) {
        System.out.println(SingletonTest.getInstance()
                                        .toString());
        System.out.println(SingletonTest.getInstance()
                                        .toString());
    }

    public static SingletonTest getInstance() {
        return Helper.instance;
    }

    private static class Helper {
        private static final SingletonTest instance = new SingletonTest();

    }
}

//GUI
class ObserverTest {
    public static void main(String[] args) {
        Observer observer = new Observer();
        Subject sub1 = new Subject();
        Subject sub2 = new Subject();
        sub1.register(observer);
        sub2.register(observer);
        sub1.setVal(10);
        sub2.setVal(20);
    }

    static class Observer {
        public void update(Subject s, int val) {
            System.out.println("subject " + s + " was updated by " + val);
        }
    }

    static class Subject {
        private int val;
        private ArrayList<Observer> observers = new ArrayList<>();

        public void register(Observer o) {
            observers.add(o);
        }

        public void unregister(Observer o) {
            observers.remove(o);
        }

        public void setVal(int val) {
            this.val = val;
            notifyObservers();
        }

        private void notifyObservers() {
            for (Observer o : observers)
                o.update(this, val);
        }
    }
}

//GUI
class DecoratorTest {
    public static void main(String[] args) {
        Component c = new Coffe().setComponent(new Milk())
                                 .setComponent(new Sugar())
                                 .setComponent(new Sugar());
        System.out.println(c.getDescription() + " " + c.getComponent());

        Component c2 = new Sugar(new Sugar(new Milk(new Coffe())));
        System.out.println(c2.getDescription() + " " + c2.getComponent());
    }

    static class Component {
        String description;
        Component parent;

        public String getDescription() {
            return parent == null ? this.description : parent.getDescription() + this.description;
        }

        public Component getComponent() {
            return this;
        }

        public Component setComponent(Component component) {
            component.parent = this;
            return component;
        }
    }

    static class Coffe extends Component {
        Coffe() {
            this.description = "Coffe ";
        }

        Coffe(Component c) {
            this();
            this.parent = c;
        }
    }

    static class Milk extends Component {
        Milk() {
            this.description = "Milk ";
        }

        Milk(Component c) {
            this();
            this.parent = c;
        }
    }

    static class Sugar extends Component {
        Sugar() {
            this.description = "Sugar ";
        }

        Sugar(Component c) {
            this();
            this.parent = c;
        }
    }
}

//AOP
class ProxyTest {
    public static void main(String[] args) {
        Proxy px = new Proxy();
        px.doSomeWork();
    }

    static abstract class Subject {
        public abstract void doSomeWork();
    }

    public static class ConcreteSubject extends Subject {
        @Override
        public void doSomeWork() {
            System.out.println(" I am from concrete subject");
        }
    }

    public static class Proxy extends Subject {
        ConcreteSubject cs;

        @Override
        public void doSomeWork() {
            System.out.println("Proxy call happening now");
            //Lazy initialization
            if (cs == null) {
                cs = new ConcreteSubject();
            }
            cs.doSomeWork();
        }
    }
}

//SQL connection or Oracle connection
class FactoryMethodTest {
    public static void main(String[] args) throws Exception {
        IConnectionFactory connFactory = new ConnectionFactory();
        connFactory.GetConnection("Oracle")
                   .connect();
        connFactory.GetConnection("Mongo")
                   .connect();
    }

    interface IConnection {
        void connect();
    }

    interface IConnectionFactory {
        IConnection GetConnection(String type) throws Exception;
    }

    static class Oracle implements IConnection {
        @Override
        public void connect() {
            System.out.println("Oracle connected");
        }
    }

    static class Mongo implements IConnection {
        @Override
        public void connect() {
            System.out.println("Mongo connected");
        }
    }

    static class ConnectionFactory implements IConnectionFactory {
        @Override
        public IConnection GetConnection(String type) throws Exception {
            switch (type) {
                case "Oracle":
                    return new Oracle();
                case "Mongo":
                    return new Mongo();
                default:
                    throw new Exception("Connection type : " + type + " cannot be instantiated");
            }
        }
    }
}