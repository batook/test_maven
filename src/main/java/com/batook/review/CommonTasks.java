package com.batook.review;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

enum Environment {
    PROD("http://prod"),
    SIT("http://sit"),
    UAT("http://uat"),
    DEV("http://dev");

    private static final Map<String, Environment> lookup = new HashMap<>();

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
        // Разность коллекций
        listDiff();
        //Fibo
        assertEquals(55, f1(10));
        assertEquals(55, f2(10));
        //Sum
        assertEquals(Arrays.stream(unsorted)
                           .sum(), IntStream.of(unsorted)
                                            .sum());
        //Max
        assertEquals(100, IntStream.of(unsorted)
                                   .max()
                                   .getAsInt());

        //Revers
        reverseArray(chr);

        //Strings
        stringMix();

        //Numbers
        numberMix();
    }

    public static int f1(int x) {
        if (x == 1 || x == 2) return 1;
        int f1 = 1, f2 = 1, res = 1;
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

    public static void listDiff() {
        // Разность коллекций
        List<Integer> a = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> b = new ArrayList(Arrays.asList(3, 4, 5, 6, 7));
        List<Integer> i = new ArrayList(a);
        i.retainAll(b); //[3, 4, 5]
        a.addAll(b); //[1, 2, 3, 4, 5, 3, 4, 5, 6, 7]
        a.removeAll(i); //[1, 2, 6, 7]
        Set<Integer> s = Stream.concat(a.stream(), b.stream())
                               .filter(e -> !i.contains(e))
                               .collect(Collectors.toSet());
        assertArrayEquals(a.toArray(), s.toArray());
    }

    public static void reverseArray(char[] ch) {
        char[] t = ch.clone();
        String s = "";
        for (int i = 0; i < t.length / 2; i++) {
            t[i] ^= t[t.length - 1 - i];
            t[t.length - 1 - i] ^= t[i];
            t[i] ^= t[t.length - 1 - i];
        }
        assertEquals("!abar eneb enE", new String(t));

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
        String text = "se f g a d f s s f d s ft gh f ws w f v x s g h d h j j k f sd j e wed a d f a r b k c d ";
        char[] chars = text.toCharArray();
        text = String.valueOf(chars);
        List<String> list = Arrays.asList(text.split("\\s+"));
        Map<String, Long> map = list.stream()
                                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        System.out.println(map);
        map.clear();
        for (String e : list) {
            long i = map.get(e) == null ? 1 : map.get(e) + 1;
            map.put(e, i);
        }
        System.out.println(map);
        map.clear();
        Set<String> set = new HashSet<>(list);
        for (String e : set)
            map.put(e, (long) Collections.frequency(list, e));
        System.out.println(map);
        Collections.sort(list);
        System.out.println(list.subList(0, 10));
        map.clear();
        map = list.stream()
                  .collect(Collectors.toMap(s -> s, s -> (long) s.length(), (i1, i2) -> i2));
        System.out.println(map);
    }

    public static void numberMix() {
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
    }

    public static void lambdaMix() {

    }
}


