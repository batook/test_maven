package com.batook.test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

interface BaseInterface1 {
    default void getName() {
        System.out.println("Base 1");
    }
}

interface BaseInterface2 {
    default void getName() {
        System.out.println("Base 2");
    }
}

interface MyInterface extends BaseInterface1, BaseInterface2 {
    default void getName() {
        System.out.println("Just me");
        BaseInterface1.super.getName();
        BaseInterface2.super.getName();
    }
}

public class Test {
    public static void main(String[] args) {
        ThreadProducer p = new ThreadProducer();
        new Thread(new ThreadConsumer(p)).start();
        new Thread(new ThreadConsumer(p)).start();
        new Thread(p).start();
        // Find duplicate
        getDublicate();
        // Sort
        int[] unsorted = {32, 39, 21, 45, 23, 3};
        bubbleSort(unsorted);

        //int[] unsortedBig = IntStream.iterate(0, i -> i + 1).limit(10).toArray();
        int[] unsortedBig = new Random().ints(1, 100).limit(100).toArray();

        // Get MAX
        List<Integer> t = Arrays.stream(unsortedBig).boxed().collect(Collectors.toList());
        int max = t.iterator().next();
        for (int i : t) {
            if (max < i) max = i;
        }
        System.out.println("max=" + max);
        max = t.stream().max(Comparator.naturalOrder()).get();
        System.out.println("max=" + max);

        // Fibonacci
        for (int i = 1; i <= 10; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        System.out.println();
    }

    /*
     * Java program for Fibonacci number using recursion.
     * This program uses tail recursion to calculate Fibonacci number for a given number
     */
    static int fibonacci(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        return fibonacci(number - 1) + fibonacci(number - 2); //tail recursion
    }

    /*
     * Java program to calculate Fibonacci number using loop or Iteration.
     */

    static int fibonacci2(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        int fibo1 = 1, fibo2 = 1, fibonacci = 1;
        for (int i = 3; i <= number; i++) {
            fibonacci = fibo1 + fibo2; //Fibonacci number is sum of previous two Fibonacci number
            fibo1 = fibo2;
            fibo2 = fibonacci;
        }
        return fibonacci; //Fibonacci number
    }


    static void bubbleSort(int[] unsorted) {
        System.out.println("unsorted array before sorting : " + Arrays.toString(unsorted));
        // Outer loop - need n-1 iteration to sort n elements
        for (int i = 0; i < unsorted.length - 1; i++) {
            //Inner loop to perform comparision and swapping between adjacent numbers
            //After each iteration one index from last is sorted
            for (int j = 1; j < unsorted.length - i; j++) {
                //If current number is greater than swap those two
                if (unsorted[j - 1] > unsorted[j]) {
                    int temp = unsorted[j];
                    unsorted[j] = unsorted[j - 1];
                    unsorted[j - 1] = temp;
                }
            }
            System.out.printf("unsorted array after %d pass %s: %n", i + 1, Arrays.toString(unsorted));
        }
    }

    static void test() {
        long startTime = System.nanoTime();
        // ... the code being measured ...
        double estimatedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println(new DecimalFormat("#.##########").format(estimatedTime));

        Arrays.asList("a b c d".split("\\s+")).forEach(System.out::println);
        Arrays.asList(Pattern.compile("\\s+").split("a b c d")).forEach(System.out::println);
        System.out.println(Stream.of("a", "b", "c").collect(Collectors.joining(",")));
        System.out.println(Stream.of("a", "b", "c").reduce("concat: ", String::concat));
        System.out.println(Stream.of(1, 2, 3, 4, 5).reduce(1, (i, j) -> i * j));

        new Random().ints(1, 10).limit(3).forEach(System.out::println);
        System.out.println(Stream.of('w', 'o', 'l', 'f').parallel().reduce("", (c, s1) -> c + s1, (a, b) -> a + b));
        System.out.println(Stream.of("w", "o", "l", "f").reduce("", String::concat));
    }

    static void top3() {
        int cnt = 0;
        List<String> src = new ArrayList<>(Arrays.asList("q q w e r t y a a b c d".split("\\s+")));
        TreeSet<String> t = new TreeSet<>(src);
        Iterator<String> i = t.iterator();
        while (i.hasNext() && cnt < 3) {
            System.out.println(i.next());
            cnt++;
        }
        List<String> res = new ArrayList<>(t).subList(0, 3);
        System.out.println(res);
    }

    static void charFreq() {
        String text = "a r b k c d se f g a d f s s f d s ft gh f ws w f v x s g h d h j j k f sd j e wed a d f";
        //
        List<String> list = Arrays.asList(text.split("\\s+"));
        Map<String, Long> m = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        m.forEach((k, v) -> System.out.println(k + ":" + v));
        //
        Set<String> set = new HashSet<>(Arrays.asList(Pattern.compile("\\s+").split(text)));
        for (String e : set)
            System.out.println(e + " " + Collections.frequency(list, e));
        //
        Map<Character, Integer> m2 = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            int cnt = m2.get(text.charAt(i)) == null ? 1 : m2.get(text.charAt(i)) + 1;
            m2.put(text.charAt(i), cnt);
        }
        System.out.println("Char count: " + m2);
    }

    static void getDublicate() {
        int[] a1 = {6, 5, 5, 4, 3, 2, 1};
        int s1 = 0, s2 = 0;
        System.out.println(Arrays.toString(a1));
        for (int i = 0; i < a1.length; i++) {
            s1 += a1[i];
        }
        s2 = IntStream.rangeClosed(1, 6).sum();
        System.out.println(a1[s1 - s2]);
    }
}


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

class ThreadConsumer implements Runnable {
    ThreadProducer p;

    public ThreadConsumer(ThreadProducer p) {
        this.p = p;
    }

    @Override
    public void run() {
        synchronized (p) {
            while (!p.isDone) {
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            result = 100;
            isDone = true;
            notifyAll();
        }

    }
}

class ArrayReversalDemo {

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7};
        reverse(numbers);
        reverseString("Ene bene raba!");
        reverseByArray("Ene bene raba!");
    }

    /**
     * reverse the given array in place * @param input
     */
    public static void reverse(int[] input) {
        System.out.println("original array : " + Arrays.toString(input));
        // handling null, empty and one element array
        if (input == null || input.length <= 1) {
            return;
        }
        for (int i = 0; i < input.length / 2; i++) {
            int temp = input[i];
            // swap numbers
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
        System.out.println("reversed array : " + Arrays.toString(input));
    }

    static void reverseString(String st) {
        System.out.println(st);
        char[] c = st.toCharArray();
        for (int i = 0; i < c.length / 2; i++) {
            char tmp = c[i];
            c[i] = c[(c.length - 1) - i];
            c[(c.length - 1) - i] = tmp;
        }
        System.out.println(new String(c));
        //
        String s = "";
        for (int i = st.length() - 1; i >= 0; i--) {
            s += st.charAt(i);
        }
        System.out.println(s);
        //
        System.out.println(new StringBuilder(st).reverse());
    }

    static void reverseByArray(String s) {
        char[] src = s.toCharArray();
        char[] tgt = new char[src.length];
        for (int i = 0; i < src.length; i++) {
            tgt[(src.length - 1) - i] = src[i];
        }
        System.out.println(new String(tgt));
    }
}

class TT implements MyInterface {
    public static void main(String[] args) {


    }

}

class Employee {
    String name = "Employee";

    void printName() {
        System.out.println(name);
    }
}

class Programmer extends Employee {
    String name = "Programmer";

    void printName() {
        System.out.println(name);
    }
}

class Office1 {
    public static void main(String[] args) {
        Employee emp = new Employee();
        Employee programmer = new Programmer();
        System.out.println(emp.name);
        System.out.println(programmer.name);
        emp.printName();
        programmer.printName();
        System.out.println(new Employee() instanceof Programmer);
        System.out.println(new Programmer() instanceof Employee);
    }
}
