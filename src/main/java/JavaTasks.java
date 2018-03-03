import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class JavaTasks {
    // https://proglib.io/p/15-questions-for-programmers/
    // http://qa7.ru/blog/2014/06/22/voprosy-po-java-na-interviu/
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        getDublicate();
        testLinkedList();
        //testing our bubble sort method in Java
        int[] unsorted = {32, 39, 21, 45, 23, 3};
        //int[] unsortedBig = IntStream.iterate(0, i -> i + 1).limit(100).toArray();
        int[] unsortedBig = new Random().ints(1, 100).limit(10).toArray();

        List<Integer> t = Arrays.stream(unsortedBig).boxed().collect(Collectors.toList());
        int max = t.iterator().next();
        for (int i : t) {
            if (max < i) max = i;
        }
        System.out.println("max=" + max);
        max = t.stream().max(Comparator.naturalOrder()).get();
        System.out.println("max=" + max);

        bubbleSort(unsortedBig);
        //one more testing of our bubble sort code logic in Java
        int[] test = {5, 3, 2, 1};
        bubbleSort(test);
        for (int i = 1; i <= 10; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        System.out.println();
        for (int i = 1; i <= 10; i++) {
            System.out.print(fibonacci2(i) + " ");
        }
        System.out.println();
        double estimatedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println(new DecimalFormat("#.##########").format(estimatedTime));
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

    public static void bubbleSort(int[] unsorted) {
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

    /*
     * Java program for Fibonacci number using recursion.
     * This program uses tail recursion to calculate Fibonacci number for a given number
     */
    public static int fibonacci(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        return fibonacci(number - 1) + fibonacci(number - 2); //tail recursion
    }

    /*
     * Java program to calculate Fibonacci number using loop or Iteration.
     */
    public static int fibonacci2(int number) {
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

    static void testLinkedList() {
        LinkedList linkedList = new JavaTasks().new LinkedList();
        linkedList.add(linkedList.new Node("1"));
        linkedList.add(linkedList.new Node("2"));
        linkedList.add(linkedList.new Node("3"));
        linkedList.add(linkedList.new Node("4"));
        linkedList.add(linkedList.new Node("5"));
        linkedList.add(linkedList.new Node("6"));
        linkedList.add(linkedList.new Node("7"));
        linkedList.add(linkedList.new Node("8"));
        linkedList.add(linkedList.new Node("9"));
        linkedList.add(linkedList.new Node("10"));
        linkedList.iterate();
        LinkedList.Node current, middle;
        //Double pass
        current = linkedList.first;
        int length = 0;
        while (current.next != null) {
            length++;
            current = current.next;
        }
        current = linkedList.first;
        for (int i = 1; i <= length / 2; i++) {
            current = current.next;
        }

        System.out.println("length of LinkedList: " + length);
        System.out.println("middle element of LinkedList : " + current);

        //Single pass
        // need to maintain two-pointer:
        // one increment at each node while other increments after two nodes at a time,
        // when first pointer reaches end, second pointer will point to middle element of linked list
        current = linkedList.first;
        middle = current;
        int element = 0;
        while (current.next != null) {
            element++;
            current = current.next;
            if (element % 2 == 0) {
                middle = middle.next;
            }
        }
        //        if (element % 2 == 1) {
        //            middle = middle.next;
        //        }
        System.out.println("length of LinkedList: " + element);
        System.out.println("middle element of LinkedList : " + middle);
    }

    public static class Palindrome {
        public static int recursion(int n) {
            if (n < 10) {
                return n;
            } else {
                System.out.print(n % 10 + " ");
                return recursion(n / 10);
            }
        }

        public static void main(String[] args) {
            System.out.println(recursion(123));
        }
    }

    public static class Palword {
        public static String recursion(String s) {
            if (s.length() == 1) {
                return "YES";
            } else {
                if (s.substring(0, 1).equals(s.substring(s.length() - 1, s.length()))) {
                    if (s.length() == 2) {
                        return "YES";
                    }
                    return recursion(s.substring(1, s.length() - 1));
                } else {
                    return "NO";
                }
            }
        }

        public static void main(String[] args) {
            System.out.println(recursion("ABBA"));
        }
    }

    class LinkedList {
        Node first;
        Node last;

        public LinkedList() {
            first = new Node("head");
            last = first;
        }

        public void add(Node node) {
            last.next = node;
            last = node;
        }

        public void iterate() {
            Node current = this.first;
            System.out.print(current.data);
            while (current.next != null) {
                System.out.print(" -> " + current.next);
                current = current.next;
            }
            System.out.println();
        }

        class Node {
            Node next;
            String data;

            public Node(String data) {
                this.data = data;
            }

            public String toString() {
                return this.data;
            }
        }
    }
}

class NestedTest {
    static int s;
    int i;
    NestedTest.A a;
    NestedTest.B b;
    NestedTest.C c;
    NestedTest.D d;


    static class C {
        static int s;
        int i;
        NestedTest.A a;
        NestedTest.B b;
        NestedTest.D d;
        int sc = NestedTest.s;
        //int ic = A.i;
        int ic = NestedTest.D.s;
    }

    private static class D {
        static int s;
        int i;
        NestedTest.A a;
        NestedTest.B b;
        NestedTest.C c;
        int sd = NestedTest.s;
    }

    class A {
        //static int s;
        int i = NestedTest.this.i;
        NestedTest.B b;
        NestedTest.C c;
        NestedTest.D d;
        int ia = new B().i;
    }

    private class B {
        //static int s;
        int i;
        NestedTest.A a;
        NestedTest.C c;
        NestedTest.D d;
        int ib = new A().i;
        int iab = NestedTest.this.new A().ia;
    }
}

class NestedTest2 {
    static int s;
    int i;
    NestedTest.A a;
    //NestedTest.B b;
    NestedTest.C c;
    //NestedTest.D d;
}

class PolyTest {
    public static void main(String[] args) {
        System.out.println(new PolyTest().new A().getI());
        System.out.println(new PolyTest().new B().getI());
        System.out.println(new PolyTest().new C().getI());
    }

    private class A {
        int i = 1;

        int getI() {
            return i;
        }
    }

    private class B extends A {
        int i;

        public B() {
            i = 2;
        }

    }

    private class C extends A {
        public C() {
            i = 3;
        }

    }
}


