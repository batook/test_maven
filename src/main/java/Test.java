import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sbaymanov on 11.10.2016.
 */
public class Test {
    private int a;

    public static void main(String[] args) {
        new Test().maxTemp(Stream.of(1));
        System.out.println(Arrays.stream("abc de".split(" ")).min(String::compareTo).get());
        Comparator<String> lengthCompare = (s1, s2) -> s1.length() - s2.length();
        System.out.println(Arrays.stream("abc de".split(" ")).min(lengthCompare).get());
        List<String> l = Arrays.stream("abc de".split(" ")).map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(l);
        Set<String> ss = Arrays.stream("a b c a b c".split(" ")).map(String::toUpperCase).collect(Collectors.toCollection(TreeSet::new));
        ss.stream().forEach(System.out::println);
        System.out.println(ss);
        Map<Integer, List<String>> m = Arrays.stream("a bb bb ccc ccc ccc".split(" ")).distinct()
                .collect(Collectors.groupingBy(String::length));
        m.forEach((count, words) -> {
            System.out.print(count);
            words.forEach(System.out::println);
        });
        Map<Boolean,List<String>> bb=Arrays.stream("a1 bb1 ccc1".split(" ")).collect(Collectors.partitioningBy(s->s.length()>2));
        System.out.println("length>1 "+bb.get(true));
        bb.get(true).forEach(System.out::println);
        bb.forEach((x,y)->{System.out.print(x); y.forEach(System.out::println);});

        BiPredicate<List<Integer>, Integer> p1 = List::contains;
        BiPredicate<List<Integer>, Integer> p2 = (x, y) -> x.contains(y);
        System.out.println("BiPredicate " + p2.test(Arrays.asList(1, 2, 3), 1));

        ObjIntConsumer<Integer> ic = (x, y) -> System.out.println(x + y);
        ic.accept(100, 100);

        Integer iOb1 = 100;
        Integer iOb2 = 100;
        System.out.println(iOb1 == iOb2);

        Integer iOb3 = new Integer(120);
        Integer iOb4 = new Integer(120);
        System.out.println(iOb3 == iOb4);

        Integer iOb5 = 200;
        Integer iOb6 = 200;
        System.out.println(iOb5 == iOb6);


        Calendar Cal = Calendar.getInstance();
        System.out.println(Cal.get(Cal.YEAR));
        int b = 10;
        new Test().go((long) b);
        String reverseString = "";
        String s = "abcd";
        for (int i = s.length() - 1; i >= 0; i--) {
            reverseString += s.charAt(i);
        }
        System.out.println(reverseString);
        System.out.println(new StringBuilder("abcd").reverse());
        char[] str = "abracadabra".toCharArray();
        for (int i = str.length - 1; i >= 0; i--) {
            System.out.println(str[i]);
        }
        B cb = new B();
        cb.addAll(3);
        System.out.println(cb.getCnt());
        System.out.println(Arrays.asList("a b r acadabra".split(" ")));

        "dbbeeecba".chars().distinct().sorted().forEach(s1 -> System.out.print((char) s1));
        System.out.println("");
        IntStream.rangeClosed(1, 10).filter(i -> (i % 2) == 0).forEach(System.out::println);
    }

    public void testOpt(Integer t) {
        System.out.println("Max=" + t);
    }

    public void maxTemp(Stream<Integer> temps) {
        Optional<Integer> max = temps.max(Integer::compareTo);
        System.out.println(max);
        System.out.println(max.isPresent() ? max.get() : "empty");
        max.ifPresent(new Test()::testOpt);
        max.ifPresent(i -> testOpt(i));
    }

    public void go(Long l) {
        System.out.println(l);
    }
}

class A {
    public void add(int i) {
        System.out.println(this + " A.add=" + i);
    }

    public void addAll(int t) {
        System.out.println(this + " A.addAll=" + t);
        //for (int i = 0; i < t; i++) {
        add(1);
        // }
    }
}

class B extends A {
    int cnt = 0;

    @Override
    public void add(int t) {
        System.out.println(this + " B.add=" + t);
        cnt = cnt + t;
        super.add(t);
    }

    @Override
    public void addAll(int t) {
        System.out.println(this + " B.addAll=" + t);
        cnt = cnt + t;
        super.addAll(t);
    }

    public int getCnt() {
        return cnt;
    }
}
