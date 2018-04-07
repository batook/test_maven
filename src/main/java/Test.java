import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public class Test {

    public static void main(String[] args) {
        IntFunction<UnaryOperator<Integer>> func = i -> j -> i * j;
        Integer apply = func.apply(10).apply(new Integer(20));
        System.out.println(apply);

    }
}

class A {
    static void t() {
        System.out.println("A");
    }
}

class B extends A {
    static void t() {
        System.out.println("B");
    }
}

