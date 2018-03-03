package com.batook.test;

class DecoratorTest {
    public static void main(String[] args) {
        Component c = new Cocnag(new Sugar(new Milk(new BlackCoffee())));
        System.out.println(c.getDescription());
        Component c2 = new BlackCoffee().addComponent(new Milk()).addComponent(new Sugar());
        System.out.println(c2.getDescription());
        System.out.println(c2);
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