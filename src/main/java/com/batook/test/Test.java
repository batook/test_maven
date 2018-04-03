package com.batook.test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Test {
    String id;

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
        //Reverse
        reverseArray();
        removeDublicate();
        test();
    }

    static void deadlock() {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B) {
                        System.out.println("Lock AB");
                    }
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A) {
                        System.out.println("Lock BA");
                    }
                }

            }
        });
        t1.start();
        t2.start();
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

    static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n - 1);
    }

    static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
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

        List<String> list = new ArrayList<String>();
        list.add("Yuri");
        list.add("Ron");
        list.add("Interview");
        list.add("Longest");
        list.add("List");
        list.add("Contain");
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(Function.identity(), String::length));
        map.entrySet().stream().filter(e -> e.getValue() == 7).forEach(System.out::println);
        Map<Integer, List<String>> map2 = list.stream().collect(Collectors.groupingBy(String::length));
        System.out.println(map2);

        Map<String, Integer> sorted2 = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return map.get(o1).compareTo(map.get(o2));
            }
        });
        sorted2.putAll(map);
        System.out.println("sorted2 " + sorted2);

        Comparator<Map.Entry<String, Integer>> valComparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        List<Map.Entry<String, Integer>> sortList = new ArrayList<>(map.entrySet());
        Collections.sort(sortList, valComparator);
        System.out.println("sortList " + sortList);
        System.out.println(sortList.stream().filter(e -> e.getValue() == 7).collect(Collectors.toList()));
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

    static void missingN() {
        //10 is missing
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12};

        int idealSum = IntStream.rangeClosed(1, 12).sum();
        int sum = Arrays.stream(numbers).sum();

        int missingNumber = idealSum - sum;
        System.out.println(missingNumber);
    }

    static void getDublicate() {
        int[] a1 = {6, 5, 5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(a1));
        int s1 = Arrays.stream(a1).sum();
        int s2 = IntStream.rangeClosed(1, 6).sum();
        System.out.println(a1[s1 - s2]);
    }

    static void removeDublicate() {
        int[] a1 = {6, 5, 5, 4, 3, 2, 2, 1, 1};
        Arrays.sort(a1);
        int t = a1[0];
        System.out.println(Arrays.toString(a1));
        for (int i = 1; i < a1.length; i++) {
            if (a1[i] != t) {
                System.out.print(t);
                t = a1[i];
            }
        }
        System.out.println();
    }

    static void reverseArray() {
        char[] t = "Ene bene raba!".toCharArray();
        for (char i = 0; i < t.length / 2; i++) {
            /*
            swap A<->B
            A ^= B;
            B ^= A;
            A ^= B;
            */
            t[i] ^= t[t.length - 1 - i];
            t[t.length - 1 - i] ^= t[i];
            t[i] ^= t[t.length - 1 - i];
        }
        System.out.println(new String(t));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return ((Test) obj).id.equals(this.id);
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
    static void reverse(int[] input) {
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
        System.out.println("Array " + new String(c));
        //
        LinkedList<Character> stack = new LinkedList<>();
        for (char ch : st.toCharArray())
            stack.push(ch);
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) sb.append(stack.pop());
        System.out.println("Stack " + sb.toString());
        //
        String s = "";
        for (int i = st.length() - 1; i >= 0; i--) {
            s += st.charAt(i);
        }
        System.out.println("Array2 " + s);
        //
        System.out.println("SB " + new StringBuilder(st).reverse());
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

class TT {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12};
        int idealSum = IntStream.rangeClosed(1, 12).sum();
        int sum = Arrays.stream(numbers).sum();
        int missingNumber = idealSum - sum;
        System.out.println(missingNumber);

        List<String> l = Arrays.asList("Ene bene raba!".split(""));
        Set<String> c = new HashSet<>(l);
        Map<String, Integer> m = new TreeMap<>();

        for (String s : c) {
            m.put(s, Collections.frequency(l, s));
        }
        System.out.println(m);
        Map<String, Integer> sorted = new TreeMap<>(new ValueComparator(m));
        sorted.putAll(m);
        System.out.println(sorted);
        Map<String, Integer> sorted2 = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return m.get(o1).compareTo(m.get(o2));
            }
        });
        sorted2.putAll(m);
        System.out.println(sorted2);
        Comparator<Map.Entry<String, Integer>> valComparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        List<Map.Entry<String, Integer>> list = new ArrayList<>(m.entrySet());
        Collections.sort(list, valComparator);
        System.out.println(list.stream().filter(e -> e.getValue() == 1).collect(Collectors.toList()));

    }

    static class ValueComparator implements Comparator<String> {
        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        @Override
        public int compare(String o1, String o2) {
            return ((Comparable) map.get(o1)).compareTo(map.get(o2));
        }
    }
}

