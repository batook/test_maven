package com.batook.review;

import java.util.ArrayList;

public class Patterns {
}

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
        Component parentComponent;

        public String getDescription() {
            return parentComponent == null ? this.description : parentComponent.getDescription() + this.description;
        }

        public Component getComponent() {
            return this;
        }

        public Component setComponent(Component component) {
            component.parentComponent = this;
            return component;
        }
    }

    static class Coffe extends Component {
        Coffe() {
            this.description = "Coffe ";
        }

        Coffe(Component c) {
            this();
            this.parentComponent = c;
        }
    }

    static class Milk extends Component {
        Milk() {
            this.description = "Milk ";
        }

        Milk(Component c) {
            this();
            this.parentComponent = c;
        }
    }

    static class Sugar extends Component {
        Sugar() {
            this.description = "Sugar ";
        }

        Sugar(Component c) {
            this();
            this.parentComponent = c;
        }
    }
}