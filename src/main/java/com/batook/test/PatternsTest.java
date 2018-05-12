package com.batook.test;

import java.util.ArrayList;
import java.util.List;


class ObserverPatternEx {
    public static void main(String[] args) {
        System.out.println("***Observer Pattern Demo***\n");
        Observer observer = new ObserverPatternEx().new Observer();
        Subject sub1 = new ObserverPatternEx().new Subject();
        Subject sub2 = new ObserverPatternEx().new Subject();
        sub1.register(observer);
        sub2.register(observer);
        System.out.println("Setting Flag = 5 ");
        sub1.setFlag(5);
        sub2.setFlag(5);
        System.out.println("Setting Flag = 15 ");
        sub2.setFlag(15);
        System.out.println("Setting Flag = 25 ");
        sub1.setFlag(25);
        sub1.unregister(observer);
        //No notification this time to o1 .Since it is unregistered.
        System.out.println("Setting Flag = 50 ");
        sub1.setFlag(50);
        sub2.setFlag(50);
    }


    class Observer {
        public void update(Object subj, int val) {
            System.out.println("flag value " + val + " changed in Subject " + subj);
        }
    }

    class Subject {
        List<Observer> observerList = new ArrayList<Observer>();
        private int _flag;

        public void setFlag(int _flag) {
            this._flag = _flag;
            //flag value changed .So notify observer(s)
            notifyObservers();
        }

        public void register(Observer o) {
            observerList.add(o);
        }

        public void unregister(Observer o) {
            observerList.remove(o);
        }

        public void notifyObservers() {
            for (int i = 0; i < observerList.size(); i++) {
                observerList.get(i)
                            .update(this, _flag);
            }
        }
    }
}

class SingletonPatternEx {
    public static void main(String[] args) {
        MakeACaptain c1 = MakeACaptain.getCaptain();
        MakeACaptain c2 = MakeACaptain.getCaptain();
        if (c1 == c2) {
            System.out.println("c1 and c2 are same instance");
        }
        Single c3 = Single.getInstance();
        Single c4 = Single.getInstance();
        if (c3 == c4) {
            System.out.println("c3 and c4 are same instance");
        }
    }

    static class MakeACaptain {
        private static MakeACaptain _captain;

        //We make the constructor private to prevent the use of "new"
        private MakeACaptain() {
        }

        public static synchronized MakeACaptain getCaptain() {
            // Lazy initialization
            if (_captain == null) {
                _captain = new MakeACaptain();
            }
            return _captain;
        }
    }

    // This method does not need to use the synchronization technique and eager initialization
    static class Single {
        private Single() {
        }

        public static Single getInstance() {
            return Helper.instance;
        }

        //Nested class is referenced after getInstance() is called
        private static class Helper {
            private static final Single instance = new Single();

        }
    }
}


// com.batook.test.ProxyPatternEx.java
class ProxyPatternEx {
    public static void main(String[] args) {
        System.out.println("***Proxy Pattern Demo***\n");
        Proxy px = new ProxyPatternEx().new Proxy();
        px.doSomeWork();
    }

    abstract class Subject {
        public abstract void doSomeWork();
    }

    // ConcreteSubject.java
    class ConcreteSubject extends Subject {
        @Override
        public void doSomeWork() {
            System.out.println(" I am from concrete subject");
        }
    }

    // Proxy.java
    class Proxy extends Subject {
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


class SandwichMaker {

    public static void main(String args[]) {
        SandwichMaker t = new SandwichMaker();
        Sandwich mySandwich = t.new WhiteBreadSandwich("White bread Sandwich");
        System.out.printf("Price of %s is $%.2f %n", mySandwich.getDescription(), mySandwich.price());

        //adding extra cheese using Decorator Pattter
        mySandwich = t.new CheeseDecorator(mySandwich);
        System.out.printf("Price of %s is $%.2f %n", mySandwich.getDescription(), mySandwich.price());
        mySandwich = t.new BaconDecorator(mySandwich);
        System.out.printf("Price of %s is $%.2f %n", mySandwich.getDescription(), mySandwich.price());
        //
        Sandwich my = t.new BaconDecorator(t.new CheeseDecorator(t.new WhiteBreadSandwich("White bread Sandwich")));
        System.out.printf("Price of %s is $%.2f %n", my.getDescription(), my.price());
    }


    abstract class Sandwich {
        protected String description;

        public String getDescription() {
            return description;
        }

        public abstract Double price();
    }

    class WhiteBreadSandwich extends Sandwich {
        public WhiteBreadSandwich(String desc) {
            description = desc;
        }

        @Override
        public Double price() {
            return new Double(3.0);
        }
    }

    class CheeseDecorator extends Sandwich {
        Sandwich currentSandwich;

        public CheeseDecorator(Sandwich sw) {
            currentSandwich = sw;
        }

        @Override
        public String getDescription() {
            return currentSandwich.getDescription() + ", Cheese";
        }

        @Override
        public Double price() {
            return currentSandwich.price() + new Double(0.50);
        }
    }

    class BaconDecorator extends Sandwich {
        Sandwich currentSandwich;

        public BaconDecorator(Sandwich sw) {
            currentSandwich = sw;
        }

        @Override
        public String getDescription() {
            return currentSandwich.getDescription() + ", Bacon";
        }

        @Override
        public Double price() {
            return currentSandwich.price() + new Double(0.50);
        }
    }

}


class TestDecorator {
    public static void main(String[] args) {
        TestDecorator t = new TestDecorator();
        Component c = t.new DecoratorC(t.new DecoratorB(t.new DecoratorA(t.new ConcreteComponent())));
        System.out.println(c.getDescription());
    }

    abstract class Component {
        Component component;
        String description = "Component";

        abstract String getDescription();
    }

    class ConcreteComponent extends Component {
        public ConcreteComponent() {
            description = "ConcreteComponent";
        }

        String getDescription() {
            return description;
        }
    }

    class DecoratorA extends Component {
        public DecoratorA(Component component) {
            this.component = component;
        }

        @Override
        String getDescription() {
            return component.getDescription() + " ,DecoratorA";
        }
    }

    class DecoratorB extends Component {
        public DecoratorB(Component component) {
            this.component = component;
        }

        @Override
        String getDescription() {
            return component.getDescription() + " ,DecoratorB";
        }
    }

    class DecoratorC extends Component {
        public DecoratorC(Component component) {
            this.component = component;
        }

        @Override
        String getDescription() {
            return component.getDescription() + " ,DecoratorC";
        }
    }
}

// com.batook.test.TemplateMethodPatternEx.java
class TemplateMethodPatternEx {
    public static void main(String[] args) {
        System.out.println("***Template Method Pattern Demo***\n");
        BasicEngineering bs = new TemplateMethodPatternEx().new ComputerScience();
        System.out.println("Computer Sc Papers:");
        bs.Papers();
        System.out.println();
        bs = new TemplateMethodPatternEx().new Electronics();
        System.out.println("Electronics Papers:");
        bs.Papers();
    }

    abstract class BasicEngineering {
        // Papers() in the template method
        public void Papers() {
            //Common Papers:
            Math();
            SoftSkills();
            //Specialized Paper:
            SpecialPaper();
        }

        //Non-Abstract method Math(), SoftSkills() are //already implemented by Template class
        private void Math() {
            System.out.println("Mathematics");
        }

        private void SoftSkills() {
            System.out.println("SoftSkills");
        }

        //Abstract method SpecialPaper() must be implemented in derived classes
        public abstract void SpecialPaper();
    }

    class ComputerScience extends BasicEngineering {
        @Override
        public void SpecialPaper() {
            System.out.println("Object Oriented Programming");
        }
    }

    // Electronics.java
    class Electronics extends BasicEngineering {
        @Override
        public void SpecialPaper() {
            System.out.println("Digital Logic and Circuit Theory");
        }
    }
}

