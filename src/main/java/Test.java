public class Test {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        if (a instanceof B) System.out.println("A is B");
        if (b instanceof A) System.out.println("B is A");

    }
}

class A {

}

class B extends A {

}