package com.batook.review;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

enum Environment {
    PROD("http://prod"),
    SIT("http://sit"),
    UAT("http://uat"),
    DEV("http://dev");

    private static Map<String, Environment> lookup = new HashMap<>();

    static {
        for (Environment e : Environment.values())
            lookup.put(e.getUrl(), e);
    }

    private String url;

    Environment(String url) {
        this.url = url;
    }

    public static Environment get(String url) {
        return lookup.get(url);
    }

    public String getUrl() {
        return url;
    }
}

public class CommonTasks {
    static final int[] unsorted = {100, -90, 32, 39, 21, 45, 23, 3, -100};
    static String str = "Ene bene raba!";
    static final char[] chr = str.toCharArray();

    public static void main(String[] args) {
        Environment prod = Environment.PROD;
        assertEquals("http://prod", prod.getUrl());
        assertEquals(Environment.PROD, Environment.get("http://prod"));

        int[] unsortedBig = new Random().ints(-100, 100)
                                        .limit(1000)
                                        .toArray();
        System.out.println(IntStream.of(unsorted)
                                    .boxed()
                                    .collect(Collectors.toList()));
        // Разность коллекций
        listDiff();

        //Fibonacci
        assertEquals(55, f1(10));
        assertEquals(55, f2(10));
        assertEquals(55, f3(10));

        //Sum
        assertEquals(Arrays.stream(unsorted)
                           .sum(), IntStream.of(unsorted)
                                            .sum());
        //Max
        assertEquals(100, IntStream.of(unsorted)
                                   .max()
                                   .getAsInt());
        //Random
        new Random().ints()
                    .limit(100)
                    .sum();
        new Random().ints(-10, 100)
                    .limit(100)
                    .sum();
        ThreadLocalRandom.current()
                         .ints()
                         .limit(100)
                         .sum();
        ThreadLocalRandom.current()
                         .nextInt();
        IntStream.generate(() -> (int) (Math.random() * 100))
                 .limit(100)
                 .sum();

        //Reverse
        reverseArray(chr);

        //Strings
        stringMix();

        //Numbers
        numberMix();

        //Lambdas
        lambdaMix();

        //Collections
        collectionsMix();

        //Palindrome
        assertTrue(isPalindrome("Was it a cat I saw?".replaceAll("\\W+", "")));
    }

    //Palindrome
    public static boolean isPalindrome(String s) {
        if (s.length() == 1) return true;
        if (s.substring(0, 1)
             .equalsIgnoreCase(s.substring(s.length() - 1))) {
            if (s.length() == 2) return true;
            return isPalindrome(s.substring(1, s.length() - 1));
        } else {
            return false;
        }
    }

    //Fibonacci
    public static long f1(int x) {
        if (x == 1 || x == 2) return 1;
        long f1 = 1, f2 = 1, res = 1;
        for (int i = 3; i <= x; i++) {
            res = f1 + f2;
            f1 = f2;
            f2 = res;
        }
        return res;
    }

    public static int f2(int x) {
        if (x == 1 || x == 2) return 1;
        return f2(x - 1) + f2(x - 2);
    }

    public static int f3(int x) {
        ForkJoinPool pool = new ForkJoinPool(4);
        class Fibo extends RecursiveTask<Integer> {
            final int n;

            Fibo(int n) {
                this.n = n;
            }

            @Override
            protected Integer compute() {
                if (n == 1 || n == 2) return 1;
                Fibo f1 = new Fibo(n - 1);
                f1.fork();
                Fibo f2 = new Fibo(n - 2);
                return f2.compute() + f1.join();
            }
        }
        ForkJoinTask<Integer> task = new Fibo(x);
        return pool.invoke(task);
    }

    // Factorial
    static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n - 1);
    }

    static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n)
                         .reduce(1, (long a, long b) -> a * b);
    }

    public static void listDiff() {
        // Разность коллекций
        List<Integer> a = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> b = new ArrayList(Arrays.asList(3, 4, 5, 6, 7));
        List<Integer> i = new ArrayList(a);
        i.retainAll(b); //[3, 4, 5]
        a.addAll(b); //[1, 2, 3, 4, 5, 3, 4, 5, 6, 7]
        a.removeAll(i); //[1, 2, 6, 7]
    }

    public static void reverseArray(char[] ch) {
        char[] t = ch.clone();
        for (int i = 0; i < t.length / 2; i++) {
            t[i] ^= t[(t.length - 1) - i];
            t[(t.length - 1) - i] ^= t[i];
            t[i] ^= t[(t.length - 1) - i];
        }
        assertEquals("!abar eneb enE", new String(t));

        String s = "";
        t = ch.clone();
        for (int i = t.length - 1; i >= 0; i--)
            s += t[i];
        assertEquals("!abar eneb enE", s);

        t = ch.clone();
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < t.length; i++)
            stack.push(t[i]);
        s = "";
        while (!stack.isEmpty()) s += stack.pop();
        assertEquals("!abar eneb enE", s);
    }

    public static void stringMix() {
        //Remove spaces
        "Was it a cat I saw?".replaceAll("\\W+", "");
        //
        String text = "se f g a d f s s f d s ft gh f ws w f v x s g h d h j j k f sd j e wed a d f a r b k c d ";
        char[] chars = text.toCharArray();
        text = String.valueOf(chars);
        List<String> list = Arrays.asList(text.split("\\s+"));
        Map<String, Long> map = list.stream()
                                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        System.out.println(map);
        //
        map.clear();
        for (String e : list) {
            long i = map.get(e) == null ? 1 : map.get(e) + 1;
            map.put(e, i);
        }
        System.out.println(map);
        //
        map.clear();
        Set<String> set = new HashSet<>(list);
        for (String e : set)
            map.put(e, (long) Collections.frequency(list, e));
        System.out.println(map);
        //
        Collections.sort(list);
        System.out.println(list.subList(0, 10));
        //
        map.clear();
        map = list.stream()
                  .collect(Collectors.toMap(s -> s, s -> (long) s.length(), (i1, i2) -> i2));
        System.out.println(map);
    }

    public static void numberMix() {
        System.out.println(new BigDecimal("0.1"));
        System.out.println(BigDecimal.valueOf(0.1));

        //Missing N
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12};
        int sum = IntStream.rangeClosed(1, 12)
                           .sum();
        int realSum = IntStream.of(numbers)
                               .sum();
        assertEquals(10, sum - realSum);

        //get Duplicates
        int[] dub = {6, 5, 4, 3, 2, 2, 1};
        sum = IntStream.rangeClosed(1, 6)
                       .sum();
        realSum = IntStream.of(dub)
                           .sum();
        assertEquals(4, dub[realSum - sum]);

        //remove Duplicates
        int[] dub2 = {6, 5, 5, 4, 3, 2, 2, 1, 1};
        List<Integer> dubFree = new ArrayList<>();
        Arrays.sort(dub2);
        int t = dub2[0];
        for (int i = 1; i < dub2.length; i++)
            if (dub2[i] != t) {
                dubFree.add(t);
                t = dub2[i];
            }
        assertEquals("[1, 2, 3, 4, 5]", dubFree.toString());

        //Duplicates list
        List<Integer> list = new ArrayList<>(Arrays.asList(6, 5, 5, 4, 3, 2, 2, 1, 1));
        list.stream()
            .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
            .entrySet()
            .stream()
            .filter(i -> i.getValue() > 1)
            .map(i -> i.getKey())
            .forEach(System.out::println);

        //DeDuplicate
        System.out.println(list.stream()
                               .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                               .keySet());
        Set<Integer> set = new HashSet<>(list);
        System.out.println(set);
    }

    public static void lambdaMix() {
        IntStream.of(1, 2, 3)
                 .sum();
        IntStream.generate(() -> (int) Math.random() * 100)
                 .limit(10)
                 .sum();
        new Random().ints()
                    .limit(10)
                    .sum();
        IntStream.range(1, 10)
                 .sum();
        IntStream.concat(IntStream.of(1, 3, 5), IntStream.of(2, 4))
                 .toArray();
        Stream.concat(Stream.of(1, 3, 5), Stream.of(2, 4, 5))
              .sorted()
              .collect(Collectors.toSet());
        IntStream.of(5, 6, 7)
                 .flatMap(i -> IntStream.range(0, i)) //012340123450123456
                 .boxed()
                 .collect(Collectors.toSet());
        Map<Integer, String> m1 = Arrays.stream("AA,BB,C,D,EE,FFF".split(","))
                                        .collect(Collectors.toMap(s -> s.length(), s -> s, (s1, s2) -> s1 + "|" + s2));
        System.out.println(m1);
        Map<Integer, List<String>> m2 = Stream.of("AA,BB,C,D,EE,FFF".split(","))
                                              .collect(Collectors.groupingBy(s -> s.length()));
        System.out.println(m2);
        Map<Integer, List<String>> m3 = Stream.of("AA,BB,C,D,EE,FFF".split(","))
                                              .collect(Collectors.groupingBy(s -> s.length(), Collectors.mapping(s -> s.toLowerCase(), Collectors.toList())));
        System.out.println(m3);
        Map<String, Integer> m4 = Stream.of("AA,BB,C,D,EE,FFF".split(","))
                                        .collect(Collectors.groupingBy(s -> s, Collectors.summingInt(s -> s.length())));
        System.out.println(m4);

        Stream.of("AA,BB,C,D,EE,FFF".split(","))
              .collect(Collectors.summingInt(s -> s.length())); //11
    }

    public static void collectionsMix() {

        String[] sArray = new String[]{"Array 1", "Array 2", "Array 3"};
        List<String> lList = new ArrayList<>(Arrays.asList(sArray));

        System.out.println("#1 iterator");
        Iterator<String> iter = lList.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        System.out.println("#2 for");
        for (int i = 0; i < lList.size(); i++) {
            System.out.println(lList.get(i));
        }

        System.out.println("#3 for advance");
        for (String temp : lList) {
            System.out.println(temp);
        }

        System.out.println("#4 while");
        int j = 0;
        while (j < lList.size()) {
            System.out.println(lList.get(j));
            j++;
        }
        System.out.println("remove");
        Iterator<String> itr = lList.iterator();
        while (itr.hasNext()) {
            itr.next();
            itr.remove();
        }
        assertEquals(0, lList.size());
        //
        int[] intArray = {1, 2, 3, 4, 5};
        IntStream.of(intArray)
                 .boxed()
                 .collect(Collectors.toList());
        Integer[] array = {1, 2, 3, 4, 5};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        Collections.reverse(list);
        list.removeIf(i -> i > 10);
        list.replaceAll(i -> i + 10);
        Collections.addAll(list, array);
        Collections.max(list);
        LinkedList<Integer> linkedList = new LinkedList<>(list);
        linkedList.iterator();
        linkedList.descendingIterator();
        //
        TreeSet<Integer> treeSet = new TreeSet<>(list);
        treeSet.headSet(5);
        treeSet.tailSet(5);
        treeSet.subSet(Integer.valueOf(5), Integer.valueOf(15));
        TreeSet<Integer> reverseTree = new TreeSet<>((i1, i2) -> i2.compareTo(i1));
        System.out.println(treeSet);
        reverseTree.addAll(treeSet);
        System.out.println(reverseTree);
        //
        HashMap<String, Double> planets = new HashMap<>();
        planets.put("Mercury", new Double(2439.7));
        planets.put("Earth", new Double(6371));
        planets.put("Saturn", new Double(58232));
        planets.put("Neptune", new Double(24622));
        planets.put("Venus", new Double(6051.8));
        planets.entrySet();
        planets.keySet();
        planets.values();
        for (Map.Entry e : planets.entrySet()) {
            e.getKey();
            e.getValue();
        }
        Iterator<Map.Entry<String, Double>> iterator = planets.entrySet()
                                                              .iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> e = iterator.next();
            e.getKey();
            e.getValue();
        }
        Set<String> keys = planets.keySet();
        Collection<Double> values = planets.values();
        //
        TreeMap<String, Double> treeMap = new TreeMap<>(planets);
        System.out.println(treeMap);
        //sort by value
        TreeMap<String, Double> reverseMap = new TreeMap<>((s1, s2) -> treeMap.get(s1)
                                                                              .compareTo(treeMap.get(s2)));
        reverseMap.putAll(treeMap);
        System.out.println(reverseMap);
        //sort by value
        List<Map.Entry<String, Double>> planetList = new ArrayList<>(planets.entrySet());
        Collections.sort(planetList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                return e1.getValue()
                         .compareTo(e2.getValue());
            }
        });
        System.out.println(planetList);
        //N-longest
        List<String> list2 = new ArrayList<>(Arrays.asList("Yuri", "Ron", "Interview", "Longest", "List", "Contain"));
        System.out.println(list2.stream()
                                .collect(Collectors.groupingBy(String::length))
                                .entrySet()
                                .stream()
                                .filter(k -> k.getKey() == 4)
                                .flatMap(entry -> entry.getValue()
                                                       .stream())
                                .collect(Collectors.toList()));
    }
}


