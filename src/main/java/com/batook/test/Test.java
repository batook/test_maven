package com.batook.test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
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

    void test() {
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
        String text = "a r b k c d se f g a d f s s f d s ft gh f ws w f v x s g h d h j j k f sd j e wed a d f";
        List<String> list = Arrays.asList(text.split("\\s+"));
        Map<String, Long> m = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        m.forEach((k, v) -> System.out.println(k + ":" + v));
        Set<String> set = new HashSet<>(Arrays.asList(text.split("\\s+")));
        for (String e : set)
            System.out.println(e + " " + Collections.frequency(list, e));
        System.out.println(Stream.of('w', 'o', 'l', 'f').parallel().reduce("", (c, s1) -> c + s1, (a, b) -> a + b));
        System.out.println(Stream.of("w", "o", "l", "f").reduce("", String::concat));
    }
}

class Dummy {
    public static void main(String[] args) {
        String s = "Ene, bene, raba!";
        Map<Character, Integer> m = new HashMap<>();
        for (int i = 0; i <= s.length() - 1; i++) {
            int c = m.get(s.charAt(i)) == null ? 1 : m.get(s.charAt(i)) + 1;
            m.put(s.charAt(i), c);
        }
        System.out.println(m);

        List<String> list = Arrays.asList(s.split(""));
        Map<String, Long> m2 = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        m2.forEach((k, v) -> System.out.println(k + ":" + v));

        Set<String> set = new HashSet<>(Arrays.asList(s.split("")));
        for (String e : set)
            System.out.println(e + " " + Collections.frequency(list, e));
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
        String s = "";
        for (int i = st.length() - 1; i >= 0; i--) {
            s += st.charAt(i);
        }
        System.out.println(s);
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
