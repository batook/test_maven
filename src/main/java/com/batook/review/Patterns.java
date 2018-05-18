package com.batook.review;

import java.util.ArrayList;


enum EasySingleton {
    INSTANCE;
}

class SingletonTest {
    private SingletonTest() {
    }

    public static SingletonTest getInstance() {
        return Helper.instance;
    }

    private static class Helper {
        private static final SingletonTest instance = new SingletonTest();

    }
}

//GUI
//Определяет зависимость типа «один ко многим» между объектами.
//при изменении состояния одного объекта все зависящие от него оповещаются об этом событии.
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

        private void notifyObservers() {
            for (Observer o : observers)
                o.update(this, val);
        }

        public void setVal(int val) {
            this.val = val;
            notifyObservers();
        }
    }
}

//GUI
//расширение функциональности объекта без определения подклассов.
class DecoratorTest {
    public static void main(String[] args) {
        Component c = new Coffe().addComponent(new Milk())
                                 .addComponent(new Sugar())
                                 .addComponent(new Sugar());
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

        public Component addComponent(Component component) {
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
// объект, который контролирует доступ к другому объекту, перехватывая все вызовы
class ProxyTest {
    public static void main(String[] args) {
        Proxy px = new Proxy();
        px.doSomeWork();
    }

    interface IWork {
        void doSomeWork();
    }

    public static class Subject implements IWork {
        @Override
        public void doSomeWork() {
            System.out.println(" I am from concrete subject");
        }
    }

    public static class Proxy implements IWork {
        Subject subj;

        @Override
        public void doSomeWork() {
            System.out.println("Proxy call happening now");
            //Lazy initialization
            if (subj == null) {
                subj = new Subject();
            }
            subj.doSomeWork();
        }
    }
}

//SQL connection or Oracle connection
//The main difference is that Abstract Factory creates factory while Factory pattern creates objects.
//Представляет интерфейс для создания объекта, но оставляет подклассам решение о том, какой класс создать.
class FactoryMethodTest {
    public static void main(String[] args) throws Exception {
        ConnectionFactory.GetConnection("Oracle")
                         .connect();
        ConnectionFactory.GetConnection("Mongo")
                         .connect();
    }

    interface IConnection {
        void connect();
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

    static class ConnectionFactory {
        public static IConnection GetConnection(String type) throws Exception {
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

